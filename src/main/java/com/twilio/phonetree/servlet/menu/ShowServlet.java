package com.twilio.phonetree.servlet.menu;


import com.twilio.twiml.Gather;
import com.twilio.twiml.Hangup;
import com.twilio.twiml.Play;
import com.twilio.twiml.Say;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.client.RestTemplate;

public class ShowServlet extends HttpServlet {
	public static String SYSTEM_NAME = "";
    @Override
    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws IOException {

        String selectedOption = servletRequest.getParameter("Digits");

        VoiceResponse response;
        switch (selectedOption) {
            case "1":
            	SYSTEM_NAME = "CIS";           	
                response = getNino();
                break;
            case "2":
            	SYSTEM_NAME = "JSA";
            	response = getNino();
               
                break;
            default:
                response = com.twilio.phonetree.servlet.common.Redirect.toMainMenu();
        }

        servletResponse.setContentType("text/xml");
        try {
            servletResponse.getWriter().write(response.toXml());
        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
    }

   /* private VoiceResponse getReturnInstructions() {
    	
    	VoiceResponse response = new VoiceResponse.Builder()
                .say(new Say.Builder(
                        "Please provide your National Insurance number.")
                        .voice(Say.Voice.ALICE)
                        .language(Say.Language.EN_GB)
                        .build())
                .say(new Say.Builder(
                        "Thank you for calling the ET Phone Home Service - the "
                        + "adventurous alien's first choice in intergalactic travel")
                        .build())
                .hangup(new Hangup())
                .build();

        return response;
        
    }*/
    
    private VoiceResponse getNino() {
    	
    	String ninoSpeech = "Please provide your National Insurance number";
    	VoiceResponse response = new VoiceResponse.Builder()
                .say(new Say.Builder(
                		ninoSpeech)
                        .voice(Say.Voice.ALICE)
                        .language(Say.Language.EN_GB)
                        .build())
                .gather(new Gather.Builder()
                        .action("/commuter/nino")
                        .numDigits(6)
                        .build())
                .play(new Play.Builder("http://howtodocs.s3.amazonaws.com/et-phone.mp3")
                        .loop(3)
                        .build())
                .build();
    	return response;
        
    }
}

