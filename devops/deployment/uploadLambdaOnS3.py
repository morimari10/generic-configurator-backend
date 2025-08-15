import time
import boto3
import os

def deploy_and_publish_lambda(function_name, function_zip_file):
    print("Deploying %s on %s"%(function_zip_file, function_name))
    # Read the contents of the ZIP file
    with open(function_zip_file, 'rb') as f:
        zip_file_contents = f.read()

    # Update the Lambda function code
    response = client.update_function_code(
        FunctionName=function_name,
        ZipFile=zip_file_contents,
    )
    print(response)

# Connect to AWS using the access key and secret access key
client = boto3.client('lambda', region_name="eu-west-1")

# Process handler lambda zip
function_name = os.environ.get("AWS-LAMBDA-FUNCTION-NAME")
function_zip_file = 'handler/build/aws/'+ os.environ.get("AWS-LAMBDA")
deploy_and_publish_lambda(function_name,function_zip_file)

