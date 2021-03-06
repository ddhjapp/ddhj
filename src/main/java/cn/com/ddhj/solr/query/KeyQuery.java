package cn.com.ddhj.solr.query;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;

import cn.com.ddhj.solr.data.SolrParams;
import cn.com.ddhj.solr.util.SolrSearch;
import cn.com.ddhj.solr.util.StringUtil;

/***
 * 关键词查询
 *
 */
public class KeyQuery {

	public static void setQuery(SolrQuery solrQuery,SolrParams params){
		String keyWord = StringUtil.StringFilter(params.getKeyWord());
		String city = StringUtil.StringFilter(params.getCity());
		
		if(StringUtils.isNotBlank(keyWord) || StringUtils.isNotBlank(city)) {
			/**是否有楼盘分值查询**/
			String price = ParamUtil.setScoreQuery(params);
			
			if(keyWord.startsWith("lp")) {
				//keyWord以lp开头,应该为楼盘编号
				if(price != null) {
					solrQuery.setQuery("("+ SolrSearch.k1 +":"+keyWord+") AND ("+price+")");
				}else {
					solrQuery.setQuery(SolrSearch.k1+":"+keyWord);
				}
				solrQuery.set("defType","edismax");
			    solrQuery.set("qf","k1^100.0");
				
			} else if(StringUtil.isNumber(keyWord)) {
				//keyWord纯数字
				if(price != null) {
					solrQuery.setQuery("("+ SolrSearch.k1 +":lp"+keyWord+" OR "+SolrSearch.s2+":*"+keyWord+"*) AND ("+price+")");
				}else {
					solrQuery.setQuery(SolrSearch.k1+":lp"+keyWord+" OR "+SolrSearch.s2+":*"+keyWord+"*");
				}
				solrQuery.set("defType","edismax");
			    solrQuery.set("qf","k1^100.0 s2^100.0");
			} else if(StringUtil.IsLetter(keyWord)){
				/**全为字母   只查询商品名称   商品名称拼音  和 拼音首字母   品牌   关键词**/
				if(price!=null){
					if(StringUtils.isNotBlank(keyWord) && StringUtils.isNotBlank(city)) {
						solrQuery.setQuery("("+SolrSearch.s1+":"+city+" AND "+SolrSearch.s2+":*"+keyWord+"*) AND ("+price+")");
					} else if(StringUtils.isBlank(keyWord) && StringUtils.isNotBlank(city)) {
						solrQuery.setQuery("("+SolrSearch.s1+":"+city+") AND ("+price+")");
					} else if(StringUtils.isNotBlank(keyWord) && StringUtils.isBlank(city)) {
						solrQuery.setQuery("(" + SolrSearch.s2 + ":*"+keyWord+"*) AND ("+price+")");
					}
					
				}else{
					if(StringUtils.isNotBlank(keyWord) && StringUtils.isNotBlank(city)) {
						solrQuery.setQuery(SolrSearch.s1+":"+city+" AND "+SolrSearch.s2+":*"+keyWord+"*");
					} else if(StringUtils.isBlank(keyWord) && StringUtils.isNotBlank(city)) {
						solrQuery.setQuery(SolrSearch.s1+":"+city);
					} else if(StringUtils.isNotBlank(keyWord) && StringUtils.isBlank(city)) {
						solrQuery.setQuery(SolrSearch.s2+":*"+keyWord+"*");
					}

				}
				solrQuery.set("defType","edismax");
				solrQuery.set("qf","s1^100.0 s2^40");
			/**字母数字组合   必须以字母开头  关键词  商品名称**/
			}else if(StringUtil.IsString(keyWord)){
//				String query = "";
//				if(keyWord.startsWith("si") || keyWord.startsWith("sf") || keyWord.startsWith("hf") || keyWord.startsWith("pf")) {
//					//按供应商查询商品.add by zht
//					if(keyWord.equals("sf03100063")) {
//						//用马甲公司搜索的LD商品,把该公司转成LD的编号
//						keyWord = "si2003";
//					}
//					query = "(" + SolrSearch.s16+":"+keyWord + ")";
//					solrQuery.set("defType","edismax");
//					solrQuery.set("qf","s16^100.0");
//				} else {
//					query = "(" + SolrSearch.s1+":*"+keyWord+"* OR "+SolrSearch.l8+":" + keyWord + ")";
//					solrQuery.set("defType","edismax");
//					solrQuery.set("qf","s1^100.0 l8^10");
//				}
//				
//				if(StringUtils.isNotBlank(price)) {
//					query += " AND ("+price+")";
//				}
//				solrQuery.setQuery(query);
			} else {
				if(price!=null){
					if(StringUtils.isNotBlank(keyWord) && StringUtils.isNotBlank(city)) {
						solrQuery.setQuery("("+SolrSearch.s1+":"+city+" AND "+SolrSearch.s2+":*"+keyWord+"*) AND ("+price+")");
					} else if(StringUtils.isBlank(keyWord) && StringUtils.isNotBlank(city)) {
						solrQuery.setQuery("("+SolrSearch.s1+":"+city+") AND ("+price+")");
					} else if(StringUtils.isNotBlank(keyWord) && StringUtils.isBlank(city)) {
						solrQuery.setQuery("(" + SolrSearch.s2 + ":*"+keyWord+"*) AND ("+price+")");
					}
					
				}else{
					if(StringUtils.isNotBlank(keyWord) && StringUtils.isNotBlank(city)) {
						solrQuery.setQuery(SolrSearch.s1+":"+city+" AND "+SolrSearch.s2+":*"+keyWord+"*");
					} else if(StringUtils.isBlank(keyWord) && StringUtils.isNotBlank(city)) {
						solrQuery.setQuery(SolrSearch.s1+":"+city);
					} else if(StringUtils.isNotBlank(keyWord) && StringUtils.isBlank(city)) {
						solrQuery.setQuery(SolrSearch.s2+":*"+keyWord+"*");
					}

				}
				solrQuery.set("defType","edismax");
				solrQuery.set("qf","s1^100.0 s2^40");
			}
//			solrQuery.set("bf", "sum(abs(i1))^1500.0  sum(sqrt(log(ms(t1))))^10.0");
			ParamUtil.setQuery(params,solrQuery);
		}
	}
}
