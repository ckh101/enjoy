package com.hbnet.fastsh.web.service.page;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @ClassName: CommonService
 * @Auther: zoulr@qq.com
 * @Date: 2019/4/22 16:54
 */
public abstract class CommonService {
    protected static final Sort CREATE_TIME_DESC = new Sort(Sort.Direction.DESC, "createTime");

    public abstract JpaSpecificationExecutor getExcutor();

    protected <T> Page page(Class<T> clz, final List<SearchFilter> searchFilters, final String[] searchFields, Pageable pageable) {
        return getExcutor().findAll(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();

                for (SearchFilter f : searchFilters) {
                    if (ArrayUtils.indexOf(searchFields, f.getFieldName()) == -1) {
                        continue;
                    }

                    switch (f.getOperator()) {
                        case EQ:
                            list.add(cb.equal(root.get(f.getFieldName()), f.getValue()));
                            break;
                        case LIKE:
                            list.add(cb.like(root.get(f.getFieldName()).as(String.class), f.getValue().toString()));
                            break;
                        case GT:
                            list.add(cb.greaterThan(root.<Comparable>get(f.getFieldName()), (Comparable)f.getValue()));
                            break;
                        case LT:
                            list.add(cb.lessThan(root.<Comparable>get(f.getFieldName()), (Comparable)f.getValue()));
                            break;
                        case GTE:
                            list.add(cb.greaterThanOrEqualTo(root.<Comparable>get(f.getFieldName()), (Comparable)f.getValue()));
                            break;
                        case LTE:
                            list.add(cb.lessThanOrEqualTo(root.<Comparable>get(f.getFieldName()), (Comparable)f.getValue()));
                            break;
                        case NEQ:
                            list.add(cb.notEqual(root.get(f.getFieldName()), f.getValue()));
                            break;
                        case IN:
                            list.add(cb.in(root.get(f.getFieldName()).in(f.getValue())));
                            break;
                        default:
                            continue;
                    }


                }

                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        }, pageable);
    }

    public List<SearchFilter> getSearchFilter(Map<String, String> maps) {
        Map<String, String> map = getParametersStartingWith(maps, "search");
        List<SearchFilter> searchFilters = SearchFilter.parse(map);

        return searchFilters;
    }

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     *
     * 返回的结果的Parameter名已去除前缀.
     */
    private static Map<String, String> getParametersStartingWith(Map<String, String> maps, String prefix) {
        HashMap<String, String> result = new HashMap<>();
        if ((maps != null)) {
            Iterator<Map.Entry<String, String>> entiySet = maps.entrySet().iterator();
            while (entiySet.hasNext()) {
                Map.Entry<String, String> entry = entiySet.next();
                String paramName = entry.getKey();
                if ("".equals(prefix) || paramName.startsWith(prefix)) {
                    String unPrefixed = paramName.substring(prefix.length());
                    String values = entry.getValue();
                    result.put(unPrefixed, values);
                }
            }
        }
        return result;
    }
}
