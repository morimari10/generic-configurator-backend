import os, boto3, time
from jinja2 import Template
import difflib

def update_lambda_permission():
    # Create or update the Lambda function's permissions
    try:
        print("Permission existing, updating rights...")
        lambda_client.add_permission(
        FunctionName=os.environ.get("AWS-LAMBDA-FUNCTION-NAME"),
        StatementId='lambda-apigw-'+ stage,
        Action='lambda:InvokeFunction',
        Principal='apigateway.amazonaws.com',
        SourceArn="arn:aws:execute-api:"+ region +":"+ accountId +":"+ apiId +"/*/*/*"
        )
    except:
        print('Error while adding lamba permission')
    print("Lambda permissions set.")
    
def compare_swagger_files(local_swagger_file, s3_swagger_file):
    # Load the contents of the local Swagger file
    with open(local_swagger_file, 'r') as local_file:
        local_contents = local_file.readlines()

    # Load the contents of the Swagger file from S3
    with open(s3_swagger_file, 'r') as s3_file:
        s3_contents = s3_file.readlines()

    # Compare the contents using difflib
    differ = difflib.Differ()
    diff = list(differ.compare(local_contents, s3_contents))

    # Check if there are differences
    has_differences = any(line.startswith(('+', '-')) for line in diff)
    
    return has_differences


def update_and_deploy_API():
    # Open Swagger YAML
    with open("swagger_api.yml", 'r') as swagger_file:
        swagger_definition = swagger_file.read()

    try:
        api_client.put_rest_api(
            restApiId=apiId,
            mode='merge',  # Utilisez 'overwrite' pour remplacer la définition existante
            body=swagger_definition
        )

        # Créez un nouveau déploiement
        response = api_client.create_deployment(
            restApiId=apiId,
            stageName=stage

        )
    except Exception as e:
        print(response)
    if response['ResponseMetadata']['HTTPStatusCode'] == 201:
        print("New deployment succsfully created.")
        s3_client.upload_file(local_swagger_file, s3Bucket, s3_key)
        print("New swagger uploaded into S3")
    else:
        print("Error une deployment creation.")

# Needed vars"""""""""""
region = os.environ.get("REGION")
accountId = os.environ.get("ACCOUNT_ID")
apiId = os.environ.get("API_GATEWAY_ID")
stage = os.environ.get("API_GATEWAY_STAGE")

api_client = boto3.client('apigateway', region_name=region)
lambda_client = boto3.client('lambda', region_name=region)
s3_client = boto3.client('s3', region_name=region)

s3Bucket = os.environ.get("FE_BUCKET")
s3_key = "swagger_api-"+ stage +".yml"
local_swagger_file = 'swagger_api.yml'
# """""""""""""""""""""""

update_lambda_permission()

print("Create Swagger file")
# Load template
with open('devops/deployment/swagger_api_tmpl.yml', 'r') as template_file:
    template = Template(template_file.read())

# Define template var to replace
variables = {
    'api_gateway_name': os.environ.get("API_GATEWAY_NAME"),
    'api_gateway_description': os.environ.get("API_GATEWAY_DESCRIPTION"),
    'api_gateway_endpoint_uri': os.environ.get("API_GATEWAY_ENDPOINT_URI")
}
# Replace vars
rendered_template = template.render(**variables)

# Save generated swagger file
with open('swagger_api.yml', 'w') as output_file:
    output_file.write(rendered_template)
print("Swagger file successfuly created !")
time.sleep(3)

# Compare Swagger with API in stage
try:
    s3_client.download_file(s3Bucket, s3_key, s3_key)
    print("Swagger present on S3")

    if compare_swagger_files(local_swagger_file, s3_key):
        print("Swagger is different, Update API, and upload new swagger")
        update_and_deploy_API()
    else:
        print("Swagger identical, no need to deploy")
except:
    print("file not found")
    update_and_deploy_API()




