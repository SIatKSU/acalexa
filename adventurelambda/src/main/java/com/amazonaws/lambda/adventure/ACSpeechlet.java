package com.amazonaws.lambda.adventure;

/**
Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

    http://aws.amazon.com/apache2.0/

or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.OutputSpeech;




/**
 * This sample shows how to create a simple speechlet for handling speechlet requests.
 */

public class ACSpeechlet implements SpeechletV2 {
	
    private static final String[] SPACE_FACTS = new String[] {
            "A year on Mercury is just 88 days long.",
            "Despite being farther from the Sun, Venus experiences higher temperatures "
                    + "than Mercury.",
            "Venus rotates counter-clockwise, possibly because of a collision in the "
                    + "past with an asteroid.",
            "On Mars, the Sun appears about half the size as it does on Earth.",
            "Earth is the only planet not named after a god.",
            "Jupiter has the shortest day of all the planets.",
            "The Milky Way galaxy will collide with the Andromeda Galaxy in about 5 "
                    + "billion years.",
            "The Sun contains 99.86% of the mass in the Solar System.",
            "The Sun is an almost perfect sphere.",
            "A total solar eclipse can happen once every 1 to 2 years. This makes them "
                    + "a rare event.",
            "Saturn radiates two and a half times more energy into space than it "
                    + "receives from the sun.",
            "The temperature inside the Sun can reach 15 million degrees Celsius.",
            "The Moon is moving approximately 3.8 cm away from our planet every year."
    };
	
	
	private static final Logger log = LoggerFactory.getLogger(ACSpeechlet.class);

	@Override
	public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
		log.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());
		// any initialization logic goes here
	}

	@Override
	public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
		log.info("onLaunch requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());
		return getWelcomeResponse();
	}

	@Override
	public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		IntentRequest request = requestEnvelope.getRequest();
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
				requestEnvelope.getSession().getSessionId());
		
		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;

		if ("WelcomeIntent".equals(intentName)) {
			return getWelcomeResponse();
		} 
		
		else if ("AMAZON.HelpIntent".equals(intentName)) {
			return getHelpResponse();
        } 
		
		else if ("AMAZON.StopIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");
            
            return SpeechletResponse.newTellResponse(outputSpeech);
        } 
		
		else if ("AMAZON.CancelIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");
            
            return SpeechletResponse.newTellResponse(outputSpeech);	
		} 

		else if ("NonsenseIntent".equals(intentName)) {
			String speechText = "I'm sorry I didn't catch that.  What did you say?";
			
            return getAskResponse("Adventure Creator", speechText);	
		} 
		
		else {
			return getAskResponse("Adventure Creator", "This is unsupported.  Please try something else.");
		}
	}

	@Override
	public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
		log.info("onSessionEnded requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());
		// any cleanup logic goes here
	}

	/**
	 * Creates and returns a {@code SpeechletResponse} with a welcome message.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getWelcomeResponse() {
		String speechText = "Welcome to Adventure Creator. Say list to list my games.";
		
		return getAskResponse("Welcome", speechText);
	}

	/**
	 * Creates a {@code SpeechletResponse} for the hello intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
/*	private SpeechletResponse getHelloResponse() {
		String speechText = "Hello world";

		// Create the Simple card content.
		SimpleCard card = getSimpleCard("Welcome", speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);

		return SpeechletResponse.newTellResponse(speech, card);
	}*/

	/**
	 * Creates a {@code SpeechletResponse} for the help intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getHelpResponse() {
		String speechText = "You can say list to list my games.";
		
		return getAskResponse("Help", speechText);
	}

	/**
	 * Helper method that creates a card object.
	 * @param title title of the card
	 * @param content body of the card
	 * @return SimpleCard the display card to be sent along with the voice response.
	 */
	private SimpleCard getSimpleCard(String title, String content) {
		SimpleCard card = new SimpleCard();
		card.setTitle(title);
		card.setContent(content);

		return card;
	}

	/**
	 * Helper method for retrieving an OutputSpeech object when given a string of TTS.
	 * @param speechText the text that should be spoken out to the user.
	 * @return an instance of SpeechOutput.
	 */
	private PlainTextOutputSpeech getPlainTextOutputSpeech(String speechText) {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		return speech;
	}

	/**
	 * Helper method that returns a reprompt object. This is used in Ask responses where you want
	 * the user to be able to respond to your speech.
	 * @param outputSpeech The OutputSpeech object that will be said once and repeated if necessary.
	 * @return Reprompt instance.
	 */
	private Reprompt getReprompt(OutputSpeech outputSpeech) {
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(outputSpeech);

		return reprompt;
	}

	/**
	 * Helper method for retrieving an Ask response with a simple card and reprompt included.
	 * @param cardTitle Title of the card that you want displayed.
	 * @param speechText speech text that will be spoken to the user.
	 * @return the resulting card and speech text.
	 */
	private SpeechletResponse getAskResponse(String cardTitle, String speechText) {
		SimpleCard card = getSimpleCard(cardTitle, speechText);
		PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
		Reprompt reprompt = getReprompt(speech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}
}

