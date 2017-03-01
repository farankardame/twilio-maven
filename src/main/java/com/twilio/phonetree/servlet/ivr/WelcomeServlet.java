package com.twilio.phonetree.servlet.ivr;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twilio.twiml.Gather;
import com.twilio.twiml.Play;
import com.twilio.twiml.Say;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;

public class WelcomeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws IOException {
    	
        VoiceResponse response = new VoiceResponse.Builder()
        		.say(new Say.Builder("Welcome to the Department" 
        				+ "Press 1 to know your next JSA Payment or " 
        				+ "Press 2 to know your address in CIS.")
        				.voice(Say.Voice.ALICE)
                        .language(Say.Language.EN_GB)
        				.build())                
        		.gather(new Gather.Builder()
                        .action("/menu/show")
                        .numDigits(1)
                        .build()).build();

        servletResponse.setContentType("text/xml");
        try {
            servletResponse.getWriter().write(response.toXml());
        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
    }
}

