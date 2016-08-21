package kr.co.jbnu.remedi.parser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import kr.co.jbnu.remedi.models.Medicine;

public class Medicine_Parser {
	
	public Medicine requestMedicine_detail(Medicine medicine)
	{
		String url = "http://drug.mfds.go.kr/html/bxsSearchDrugProduct.jsp?item_Seq="+medicine.getCodeNumber();
	
		Document doc = requestHtmlDoc(url);
		
		Elements info = doc.select("#column_center table tbody tr table");
		
		Element nomalInfo = info.get(1);
		Element detailInfo = info.get(5);
		Element keepInfo = info.get(6);
		
		//System.out.println(detailInfo);
		
		String[] nomalData = parseNomalInfo(nomalInfo);
		String[] detailData = parseDetailData(detailInfo);
		
		String icon = nomalData[1];
		String shape = nomalData[2];
		String categoryCode = nomalData[5];
		String permissionDate = nomalData[6];
		String standardCode = nomalData[8];
		
		String effect = detailData[0];
		String usage = detailData[1];
		String attention = detailData[2];
		
		
		medicine.addInfo(icon, shape, categoryCode, permissionDate, standardCode, effect, usage, attention);
		
		return medicine;
	}
	
	private String[] parseNomalInfo(Element nomalInfo)
	{
		Elements nomalInfoDatas = nomalInfo.select("tr td");
		
		String[] nomalInfoData = {
				nomalInfoDatas.get(2).text(),//제품명
				nomalInfoDatas.get(5).text(),//성상
				nomalInfoDatas.get(8).text(),//장방형
				nomalInfoDatas.get(11).text(),//업체명
				nomalInfoDatas.get(14).text(),//전문/일반
				nomalInfoDatas.get(17).text(),//분류번호
				nomalInfoDatas.get(20).text(),//허가일
				nomalInfoDatas.get(23).text(),//품목기준코드
				nomalInfoDatas.get(26).text()//표준코드
		};
		/*
		for(String t : nomalInfoData )
		{
			System.out.println(t);
		}
		*/
		return nomalInfoData;
	}
	
	private String[] parseDetailData(Element detailInfo)
	{
		Elements detailDatas = detailInfo.select("tbody div");
		
		String[] detailData = {
				detailDatas.get(0).text().split("효능효과 ")[1],//효능효과
				detailDatas.get(1).text().split("용법용량 ")[1],//용법용량
				detailDatas.get(2).text().split("주의사항 ")[1]//사용상의 주의사항
		};
		/*
		for( String t : detailData)
		{
			System.out.println(t);
		}*/
		
		return detailData;
	}

	
	public ArrayList<Medicine> requestMedicines(String q_name)
	{
		ArrayList<Medicine> medicines = new ArrayList<>();
		
		String url = "http://drug.mfds.go.kr/html/search_detail.jsp?startCount=0&mode=basic&sort=RANK&collection=kifdadetail&pagePerList=100&item="+q_name;
		
		Document doc = requestHtmlDoc(url);
        
		Elements medicineDatas = doc.select(".s_result_list");
       
		for(Element medicineData : medicineDatas)
		{
			//System.out.println(medicineData.text());
    	   
			Elements datas = medicineData.select("td");
			
			String name = datas.get(1).text();//이름
			String ingredient = datas.get(2).text();//주성분
			String ingredientCount = datas.get(3).text();//주성분수
    	   	String enterprise = datas.get(4).text();//기업명
    	   	String category = datas.get(5).text();//구분
    	   	String codeNumber = datas.get(1).select("a").toString().split("'")[1];//품목기준코드
    	   	
    	   	Medicine medicine = new Medicine(name,ingredient,ingredientCount,enterprise,category,codeNumber);
    	   	
    	   	medicines.add(medicine);
		}
		return medicines;
	}
	
	private Document requestHtmlDoc(String url)
	{
		String ResponseText = null;
		HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        HttpResponse response = null;
		try 
		{
			response = client.execute(get);
			
			HttpEntity resEntity = response.getEntity();
		     
		    if (resEntity != null) 
		    	ResponseText = EntityUtils.toString(resEntity, "UTF-8");
		     
		} catch (IOException e) {
			e.printStackTrace();
		}
     
        Document doc = Jsoup.parse(ResponseText);
        
        return doc;
	}
	
}
