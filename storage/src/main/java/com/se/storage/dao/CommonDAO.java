package com.se.storage.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.se.domain.DBEntity;

import java.util.List;

/**
 * The DAO layer contract for interacting with storage.
 *
 * @param <T> Entity type.
 */
public interface CommonDAO<T extends DBEntity> {

    /**
     * Stores entity into storage.
     *
     * @param entity to store.
     */
    void saveItem(T entity);

    /**
     * Gets entity from storage by id.
     *
     * @param id id.
     * @return entity.
     */
    T getItem(String id);

    /**
     * Gets entity with atomic counter from storage by id, with atomic counter update.
     * Counter is reset on {@link counterMaxValue} overflow.
     *
     * @param id                id.
     * @param counterPath       atomic counter path as a java class property path (ie : "innerData[2].counter").
     * @param counterMaxValue   atomic counter max value.
     * @param initialEntity     the initial atomic counter entity if it has to be created, else 'null'.
     * @return entity.
     */
    T getItemWithAtomicCounterAndUpdate(String id, String counterPath, long counterMaxValue, T initialEntity);

    /**
     * Gets entity with atomic counter from storage by id, with atomic counter update.
     * Counter is reset on {@link counterMaxValue} overflow.
     *
     * @param entityType        The item type.
     * @param id                id.
     * @param counterPath       atomic counter path as a java class property path (ie : "innerData[2].counter").
     * @param counterMaxValue   atomic counter max value.
     * @param initialEntity     the initial atomic counter entity if it has to be created, else 'null'.
     * @return entity.
     */
    <ItemTypeT extends T> ItemTypeT getItemWithAtomicCounterAndUpdate(Class<ItemTypeT> entityType,
        String id, String counterPath, long counterMaxValue, ItemTypeT initialEntity);

    /**
     * Gets entities from storage.
     *
     * @param itemsToGet items with primary key populated.
     * @return entities.
     */
    List<T> getItems(List<T> itemsToGet);

    /**
     * Scans entities using the specified {@link DynamoDBScanExpression}.
     *
     * @param scanExpression the scan expression.
     * @return scanned entities.
     */
    List<T> scanItems(DynamoDBScanExpression scanExpression);
}
