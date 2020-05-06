package com.hbnet.fastsh.web.service.impl;

import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Service
public class DuoDuoTiOrderMapService {
    @PersistenceContext(unitName = "entityManagerFactorySmartAd")
    protected EntityManager em;

    public List<Map<String, Object>> getDistrictOrderCounts(){
        Query nativeQuery=em.createNativeQuery("select * from ddt_city_count order by ordernums desc");
        nativeQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> data = nativeQuery.getResultList();
        return data;
    }

    public List<Map<String, Object>> searchOrdersByParams(Map<String, String> params){
        StringBuilder sql = new StringBuilder("select * from ddt_order where 1 = 1");
        if(!params.isEmpty()){
            for(Map.Entry entry:params.entrySet()){
                sql.append(" and ").append(entry.getKey()).append("=").append("\""+entry.getValue()+"\"");
            }
            Query nativeQuery=em.createNativeQuery(sql.toString());
            nativeQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map<String, Object>> data = nativeQuery.getResultList();
            return data;
        }else{
            return null;
        }

    }
}
