package com.twilio.phonetree.servlet.commuter;

import com.twilio.phonetree.servlet.common.Redirect;
import com.twilio.phonetree.servlet.menu.ShowServlet;
import com.twilio.twiml.Dial;
import com.twilio.twiml.Hangup;
import com.twilio.twiml.Number;
import com.twilio.twiml.Say;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DobServlet extends HttpServlet {
	public static String DOB = "";
	public static String CIS_API_BASE ="https://mchannelplatform-prod.apigee.net/cis-details/cis?";
	public static String JSA_API_BASE ="https://mchannelplatform-prod.apigee.net/cis/customer?";
   /* @Override
    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws IOException {

        String selectedOption = servletRequest.getParameter("Digits");
        Map<String, String> optionPhones = new HashMap<>();
        optionPhones.put("2", "+12024173378");
        optionPhones.put("3", "+12027336386");
        optionPhones.put("4", "+12027336637");

        VoiceResponse twiMLResponse = optionPhones.containsKey(selectedOption)
                ? dial(optionPhones.get(selectedOption))
                : Redirect.toMainMenu();

        servletResponse.setContentType("text/xml");
        try {
            servletResponse.getWriter().write(twiMLResponse.toXml());
        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
    }*/

	
    /*private VoiceResponse dial(String phoneNumber) {
        Number number = new Number.Builder(phoneNumber).build();
        return new VoiceResponse.Builder()
                .dial(new Dial.Builder().number(number).build())
                .build();
    }*/
	
	@Override
    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws IOException {

        String selectedOption = servletRequest.getParameter("Digits");

        String  speech_text = "";
        String apiResponse ="";
        String test_url ="";
        
        RestTemplate restTemplate = new RestTemplate(); 
        
        String new_dob = "";
        System.out.println("selectedOption"+selectedOption);
        
		if (ShowServlet.SYSTEM_NAME == "JSA"){
			 System.out.println("doPost"+"DobServlet"+"JSA");
             test_url = JSA_API_BASE +  "nino=" + NinoServlet.NINO + "&apikey=im2IurZLr5YT2dgsmKPXGJcnMsn9ado8";
           
             apiResponse = restTemplate.getForObject(test_url, String.class);
             System.out.println(apiResponse);
             JSONObject jsonObj = new JSONObject(apiResponse);
             JSONObject resp = (JSONObject)jsonObj.getJSONObject("getCustomerResponse");
             JSONObject cust = resp.getJSONObject("customer");
             System.out.println("cust"+cust.toString());
             System.out.println("cust-dob"+cust.getString("dob"));
             new_dob = cust.getString("dob").replaceAll("-", "").replace("Z", "");
             speech_text = "Hi "+cust.getString("firstName") +" "+ cust.getString("lastName")+ " your next Payment is "+ cust.getString("amount") +" and due on "+ cust.getString("paymentDate").replaceAll("Z", "");
            System.out.println("dob"+new_dob);
            System.out.println(speech_text);
            } 
        
        if (ShowServlet.SYSTEM_NAME == "CIS"){
       	    System.out.println("doPost"+"DobServlet"+"CIS");
            test_url = CIS_API_BASE +  "nino=" + NinoServlet.NINO + "&apikey=im2IurZLr5YT2dgsmKPXGJcnMsn9ado8";
            apiResponse = restTemplate.getForObject(test_url, String.class);
            JSONObject jsonObj = new JSONObject(apiResponse);
            JSONObject resp = (JSONObject)jsonObj.getJSONObject("getCisResponse");
            JSONObject cust = resp.getJSONObject("cisdetails");
            System.out.println("cust"+cust.toString());
            System.out.println("cust-dob"+cust.getString("dob"));
            new_dob = cust.getString("dob").replaceAll("-", "").replace("Z", "");
            speech_text = "Hi "+cust.getString("firstName") +" "+ cust.getString("lastName")+ " your address is "+ cust.getString("addressline1") +" "+ cust.getString("addressline2")+" "+cust.getString("addressline3")+" " +cust.getString("city")+" "+cust.getString("postcode")+" "+cust.getString("country");
            System.out.println(speech_text);
        }
        
        System.out.println("dob"+new_dob);
        System.out.println("selectedOption"+selectedOption);
        if (new_dob.equalsIgnoreCase(selectedOption)){
        	System.out.println("comparison true");
        	 System.out.println(speech_text);
        }
        else{
        	System.out.println("comparison false");
        	speech_text = "Sorry I am not sure who you are please try again";}
        
        VoiceResponse response = new VoiceResponse.Builder()
                .say(new Say.Builder(
                        speech_text)
                        .voice(Say.Voice.ALICE)
                        .language(Say.Language.EN_GB)
                        .build())
                .say(new Say.Builder(
                        "Thank you for calling the Department for Work and Pensions")
                        .build())
                .hangup(new Hangup())
                .build();
        servletResponse.setContentType("text/xml");
        try {
            servletResponse.getWriter().write(response.toXml());
        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
    }
}

