package com.hbnet.fastsh.web.service.base;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class SpecificationFactory {

    /**
     * 单where条件
     *
     * @param p
     * @return
     */
    public static Specification where(Predication p) {
        List<Predication> ps = new ArrayList<>();
        ps.add(p);
        return where(ps);
    }

    /**
     * 多where条件and连接
     *
     * @param ps
     * @param <T>
     * @return
     */
    public static <T> Specification<T> where(List<Predication> ps) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.and(getPredicateList(root, builder, ps));
    }

    /**
     * 多where条件or连接
     *
     * @param ps
     * @param <T>
     * @return
     */
    public static <T> Specification<T> or(List<Predication> ps) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.or(getPredicateList(root, builder, ps));
    }

    /**
     * 获取查询条件数组
     *
     * @param root
     * @param builder
     * @param ps
     * @return
     */
    private static Predicate[] getPredicateList(Root<?> root, CriteriaBuilder builder, List<Predication> ps) {
        List<Predicate> predicateList = new ArrayList<>();
        ps.forEach(p -> {
            Predicate predicate = buildPredicate(builder, root.get(p.getName()), p);
            predicateList.add(predicate);
        });
        return predicateList.toArray(new Predicate[predicateList.size()]);
    }

    /**
     * 选取查询方式
     *
     * @param cb
     * @param path
     * @param p
     * @return
     */
    private static Predicate buildPredicate(CriteriaBuilder cb, Path path, Predication p) {
        Predicate predicate;
        switch (p.getOperator()) {
            case LIKE:
                predicate = cb.like(path, p.getValue().toString());
                break;
            case EQ:
                predicate = cb.equal(path, p.getValue());
                break;
            case NOTEQ:
                predicate = cb.notEqual(path, p.getValue());
                break;
            case GT:
                predicate = cb.greaterThan(path, (Comparable) p.getValue());
                break;
            case GTEQ:
                predicate = cb.greaterThanOrEqualTo(path, (Comparable) p.getValue());
                break;
            case LT:
                predicate = cb.lessThan(path, (Comparable) p.getValue());
                break;
            case LTEQ:
                predicate = cb.lessThanOrEqualTo(path, (Comparable) p.getValue());
                break;
            case NULL:
                predicate = cb.isNull(path);
                break;
            case NOTNULL:
                predicate = cb.isNotNull(path);
                break;
            case IN:
                predicate = getIn(path, p.getValue());
                break;
            case NOTIN:
                predicate = getIn(path, p.getValue()).not();
                break;
            default:
                throw new IllegalArgumentException("非法的操作符");
        }
        return predicate;
    }

    /**
     * 创建in操作
     *
     * @param path
     * @param value
     * @param <T>
     * @return
     */
    private static <T> Predicate getIn(Path path, T value) {
        if (value instanceof Object[]) {
            return path.in((Object[]) value);
        } else if (value instanceof Collection) {
            return path.in((Collection) value);
        } else {
            throw new IllegalArgumentException("非法的IN操作");
        }
    }

    /***********************************************单where条件查询********************************************************/

    // like
    public static Specification like(String name, String value) {
        return (root, query, cb) ->
                cb.like(root.get(name), value);
    }

    // =
    public static Specification equal(String name, Object value) {
        return (root, query, cb) ->
                cb.equal(root.get(name), value);
    }

    // !=
    public static Specification notEqual(String name, Object value) {
        return (root, query, cb) ->
                cb.notEqual(root.get(name), value);
    }

    // >
    public static Specification gt(String name, Object value) {
        return (root, query, cb) ->
                cb.greaterThan(root.get(name), (Comparable) value);
    }

    // >=
    public static Specification gtEqual(String name, Object value) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get(name), (Comparable) value);
    }

    // <
    public static Specification lt(String name, Object value) {
        return (root, query, cb) ->
                cb.lessThan(root.get(name), (Comparable) value);
    }

    // <=
    public static Specification ltEqual(String name, Object value) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get(name), (Comparable) value);
    }

    // is null
    public static Specification isNull(String name) {
        return (root, query, cb) ->
                cb.isNull(root.get(name));
    }

    // is not null
    public static Specification notNull(String name) {
        return (root, query, cb) ->
                cb.isNotNull(root.get(name));
    }

    // in
    public static Specification in(String name, Object value) {
        return (root, query, cb) ->
                root.get(name).in(value);
    }

    // not in
    public static Specification notIn(String name, Object value) {
        return (root, query, cb) ->
                root.get(name).in(value).not();
    }
}
