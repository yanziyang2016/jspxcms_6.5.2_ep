package com.jspxcms.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.Product;
//import com.jspxcms.core.domain.Order;
import com.jspxcms.core.domain.ProductRecord;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.repository.InfoDao;
import com.jspxcms.core.repository.ProductDao;
import com.jspxcms.core.repository.RecordDao;
import com.jspxcms.core.repository.UserDao;
import com.jspxcms.core.service.RecordService;

/**
 * UserServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class RecordServiceImpl implements RecordService{
	private static final int SALT_SIZE = 8;
	protected final static Logger logger = LoggerFactory.getLogger(RecordServiceImpl.class);
	public Page<ProductRecord> findPage(Map<String, String[]> params,
			Pageable pageable) {
		Page<ProductRecord> orderPage=dao.findAll(spec( params), pageable);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");  
		if(orderPage.getContent().size()>0){
			for(int i=0;i<orderPage.getContent().size();i++){
				User usertemp = userdao.findOne(orderPage.getContent().get(i).getUserId());
				orderPage.getContent().get(i).setUserName(usertemp.getUsername());
				orderPage.getContent().get(i).setAddDateString(sdf.format(orderPage.getContent().get(i).getAddDate()));
			}
			
		}
		return orderPage;
	}
	

	
	private Specification<ProductRecord> spec( Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<ProductRecord> fsp = SearchFilter.spec(filters, ProductRecord.class);
		Specification<ProductRecord> sp = new Specification<ProductRecord>() {
			public Predicate toPredicate(Root<ProductRecord> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
							
				return pred;
			}
		};
		return sp;
	}
	
	public Page<ProductRecord> findPage(Integer infoId, Integer infoPeriod, Pageable pageable) {
		Map<String, String[]> params = new HashMap<String, String[]>();
		Page<ProductRecord> orderPage=dao.findAll(spec1( params,infoId,infoPeriod), pageable);
		if(orderPage.getContent().size()>0){
			for(int i=0;i<orderPage.getContent().size();i++){
				User usertemp = userdao.findOne(orderPage.getContent().get(i).getUserId());
				orderPage.getContent().get(i).setUserName(usertemp.getUsername());
			}
			
		}
		
		return orderPage;
	}
	
	private Specification<ProductRecord> spec1( Map<String, String[]> params, final Integer infoId, final Integer infoPeriod) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<ProductRecord> fsp = SearchFilter.spec(filters, ProductRecord.class);
		Specification<ProductRecord> sp = new Specification<ProductRecord>() {
			public Predicate toPredicate(Root<ProductRecord> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				pred = cb.and(pred, cb.equal(root.<Integer> get("infoId"), infoId));	
				pred = cb.and(pred, cb.equal(root.<Integer> get("infoPeriod"), infoPeriod));	
				return pred;
			}
		};
		return sp;
	}

	
	




	

	private UserDao userdao;
	@Autowired
	public void setUserdao(UserDao userdao) {
		this.userdao = userdao;
	}
	
	private InfoDao infodao;
	@Autowired
	public void setInfodao(InfoDao infodao) {
		this.infodao = infodao;
	}
	
	
	private RecordDao dao;
	@Autowired
	public void setDao(RecordDao dao) {
		this.dao = dao;
	}

	@Override
	public int findCountByInfoId(Integer id) {
		
		return dao.findCountByInfoId(id);
	}
	
	@Override
	public int findPeriodByInfoId(Integer id) {
		
		return dao.findPeriodByInfoId(id);
	}

	@Transactional
	public ProductRecord save(ProductRecord bean) {
		User user = userdao.findOne(bean.getUserId());
		user.setYuanBao(user.getYuanBao()-100);
		user.setMemStatus(5);
		userdao.save(user);
		return dao.save(bean);
		
	}

	@Override
	public int findCountByInfoIdAndPeriod(Integer id, Integer infoPeriod) {
		return dao.findCountByInfoIdAndPeriod(id,infoPeriod);
	}

	@Override
	public int findByProInFo(int orderprono, Integer id, Integer infoPeriod) {
		return dao.findByProInFo(orderprono, id, infoPeriod);
	}



	@Override
	public List<ProductRecord> getByInfoId(Integer id) {
		return dao.getByInfoId(id);
	}

	
	
	
	
	

}
