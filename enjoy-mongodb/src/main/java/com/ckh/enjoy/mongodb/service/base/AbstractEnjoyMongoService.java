package com.ckh.enjoy.mongodb.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class AbstractEnjoyMongoService<T> {

    /**
     * spring mongodb　集成操作类　
     */
    @Autowired
    protected MongoTemplate mongoTemplate;

    /**
     * 通过条件查询实体(集合)
     *
     * @param query
     */
    public List<T> find(Query query) {
        return mongoTemplate.find(query, this.getEntityClass());
    }

    public List<T> find(Query query, String collectionName) {
        return mongoTemplate.find(query, this.getEntityClass(), collectionName);
    }

    /**
     * 通过一定的条件查询一个实体
     *
     * @param query
     * @return
     */
    public T findOne(Query query) {
        return mongoTemplate.findOne(query, this.getEntityClass());
    }

    public T findOne(Query query, String collectionName) {
        return mongoTemplate.findOne(query, this.getEntityClass(), collectionName);
    }

    /**
     * 通过条件查询更新数据
     *
     * @param query
     * @param update
     * @return
     */
    public void updateFirst(Query query, Update update) {
        mongoTemplate.updateFirst(query, update, this.getEntityClass());
    }

    public void updateMulti(Query query, Update update) {
        mongoTemplate.updateMulti(query, update, this.getEntityClass());
    }

    public void removeById(String id, String collectionName){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, collectionName);
    }

    /**
     * 根据数据对象中的id删除数据，集合为数据对象中@Document 注解所配置的collection
     *
     * @param obj
     *            数据对象
     */
    public void remove(T obj) {
       mongoTemplate.remove(obj);
    }

    public void remove(Criteria criteria){
        Query query = new Query();
        query.addCriteria(criteria);
        mongoTemplate.remove(query, this.getEntityClass());
    }


    /**
     * 指定集合 根据数据对象中的id删除数据
     *
     * @param obj
     *            数据对象
     * @param collectionName
     *            集合名
     */

    /**
     * 指定集合 根据数据对象中的id删除数据
     *
     * @param obj
     *            数据对象
     * @param collectionName
     *            集合名
     */
    public void remove(T obj, String collectionName) {
        mongoTemplate.remove(obj, collectionName);
    }

    /**
     * 保存一个对象到mongodb
     *
     * @param entity
     * @return
     */
    public T save(T entity) {
        mongoTemplate.insert(entity);
        return entity;
    }


    public T save(T entity, String collectionName) {
        mongoTemplate.insert(entity, collectionName);
        return entity;
    }

    public List<T> saveAll(List<T> list){
        mongoTemplate.insertAll(list);
        return list;
    }

    /**
     * 通过ID获取记录
     *
     * @param id
     * @return
     */
    public T findById(String id) {
        return mongoTemplate.findById(id, this.getEntityClass());
    }

    /**
     * 通过ID获取记录,并且指定了集合名(表的意思)
     *
     * @param id
     * @param collectionName
     *            集合名
     * @return
     */
    public T findById(String id, String collectionName) {
        return mongoTemplate.findById(id, this.getEntityClass(), collectionName);
    }

    /**
     * 分页查询
     * @param page
     * @param query
     * @return
     */
    public void findPage(PageInfo<T> page, Query query, Sort sort){
        long count = this.count(query);
        page.setTotalCount(count);
        int pageNumber = page.getPageNumber();
        int pageSize = page.getPageSize();
        query.skip((pageNumber - 1) * pageSize).limit(pageSize);
        query.with(sort);
        List<T> rows = this.find(query);
        page.setList(rows);
    }

    public void findPage(PageInfo<T> page, Query query, String collectionName){
        long count = this.count(query, collectionName);
        page.setTotalCount(count);
        int pageNumber = page.getPageNumber();
        int pageSize = page.getPageSize();
        query.skip((pageNumber - 1) * pageSize).limit(pageSize);
        List<T> rows = this.find(query, collectionName);
        page.setList(rows);
    }

    /**
     * 根据条件查询出所有结果集 集合为数据对象中@Document 注解所配置的collection
     *
     * @param obj
     *            数据对象
     * @param findKeys
     *            查询条件 key
     * @param findValues
     *            查询条件 value
     * @return
     */
    public List<T> find(T obj, String[] findKeys, Object[] findValues) {

        Criteria criteria = null;
        for (int i = 0; i < findKeys.length; i++) {
            if (i == 0) {
                criteria = Criteria.where(findKeys[i]).is(findValues[i]);
            } else {
                criteria.and(findKeys[i]).is(findValues[i]);
            }
        }
        Query query = Query.query(criteria);
        List<T> resultList = mongoTemplate.find(query, this.getEntityClass());
        return resultList;
    }

    /**
     * 指定集合 根据条件查询出所有结果集
     *
     * @param obj
     *            数据对象
     * @param findKeys
     *            查询条件 key
     * @param findValues
     *            查询条件 value
     * @param collectionName
     *            集合名
     * @return
     */
    public List<T> find(T obj, String[] findKeys, Object[] findValues, String collectionName) {

        Criteria criteria = null;
        for (int i = 0; i < findKeys.length; i++) {
            if (i == 0) {
                criteria = Criteria.where(findKeys[i]).is(findValues[i]);
            } else {
                criteria.and(findKeys[i]).is(findValues[i]);
            }
        }
        Query query = Query.query(criteria);
        List<T> resultList = mongoTemplate.find(query, this.getEntityClass(), collectionName);
        return resultList;
    }

    /**
     * 指定集合 根据条件查询出所有结果集 并排倒序
     *
     * @param obj
     *            数据对象
     * @param findKeys
     *            查询条件 key
     * @param findValues
     *            查询条件 value
     * @param collectionName
     *            集合名
     * @param sort
     *            排序字段
     * @return
     */
    public List<T> find(T obj, String[] findKeys, Object[] findValues, Sort sort) {

        Criteria criteria = null;
        for (int i = 0; i < findKeys.length; i++) {
            if (i == 0) {
                criteria = Criteria.where(findKeys[i]).is(findValues[i]);
            } else {
                criteria.and(findKeys[i]).is(findValues[i]);
            }
        }
        Query query = Query.query(criteria);
        query.with(sort);
        List<T> resultList = mongoTemplate.find(query, this.getEntityClass());
        return resultList;
    }


    /**
     * 指定集合 根据条件查询出符合的第一条数据
     *
     * @param obj
     *            数据对象
     * @param findKeys
     *            查询条件 key
     * @param findValues
     *            查询条件 value
     * @param collectionName
     *            集合名
     * @return
     */
    public T findOne(T obj, String[] findKeys, Object[] findValues) {

        Criteria criteria = null;
        for (int i = 0; i < findKeys.length; i++) {
            if (i == 0) {
                criteria = Criteria.where(findKeys[i]).is(findValues[i]);
            } else {
                criteria.and(findKeys[i]).is(findValues[i]);
            }
        }
        Query query = Query.query(criteria);
        T resultObj = mongoTemplate.findOne(query, this.getEntityClass());
        return resultObj;
    }

    /**
     * 查询出所有结果集 集合为数据对象中 @Document 注解所配置的collection
     *
     * @param obj
     *            数据对象
     * @return
     */
    public List<T> findAll(T obj) {

        List<T> resultList = mongoTemplate.findAll(this.getEntityClass());
        return resultList;
    }

    /**
     * 求数据总和
     * @param query
     * @return
     */
    public long count(Query query){
        return mongoTemplate.count(query, this.getEntityClass());
    }

    public long count(Query query, String collectionName){
        return mongoTemplate.count(query, this.getEntityClass(), collectionName);
    }


    /**
     * 获取需要操作的实体类class
     *
     * @return
     */
    private Class<T> getEntityClass(){
        Type superclass = this.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType)superclass).getActualTypeArguments();
        return (Class) actualTypeArguments[0];
    }
}
