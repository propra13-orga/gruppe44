package Main;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sounds {

	private final Houston houston;
	AudioInputStream myInputStream;
	AudioFormat myAudioFormat;
	Clip myClip;
	Clip[] clips;

	public enum Type {
		MAIN_MENU(0),
		DOOR(1),
		ATTACK(2),
		BUTTON_CLICK(3),
		MAGIC(4),
		GAME(5),
		SHOP(6);

		private int code;

		private Type(int c) {
			code = c;
		}

		public int getCode() {
			return code;
		}
	}


	// das Clip Array wird spaeter noch verkleinert, und die Zahlen des Arrays umbennant
	// zum aendern der Musik den Pfad einfach in das Array packen
	public Sounds(Houston houston) {
		this.houston = houston;
		//inizialisiert die Clips in ein Array
		clips = new Clip[EnumSet.allOf(Type.class).size()];
		clips [Type.MAIN_MENU.getCode()] = inizialeSound( "./res/sounds/dark_house_break_loop_with_drums_and_bass_120_bpm.wav");
		clips [Type.DOOR.getCode()] = inizialeSound("./res/sounds/door_hinge_creak_version_5.wav");
		clips [Type.ATTACK.getCode()] = inizialeSound("./res/sounds/knife_stab.wav");
		clips [Type.BUTTON_CLICK.getCode()] = inizialeSound("./res/sounds/portable_heater_radiator_switch_click_001.wav");
		clips [Type.MAGIC.getCode()] = inizialeSound("./res/sounds/fire_torch_pass_by_large_heavy_with_crackle_1.wav");
		clips [Type.GAME.getCode()] = inizialeSound("./res/sounds/the_underworld_dark_track_with_electro_orchestral_percussion_crime_thriller_etc.wav");
		//muss noch einen andren Sound suchen, deswegen erst mal so
		clips [Type.SHOP.getCode()] = inizialeSound("./res/sounds/dark_house_break_loop_with_drums_and_bass_120_bpm.wav");

		setSound();
	}

	//wandelt den Path in einen Sound Clip umder abgespielt werden kann

	public Clip inizialeSound(String adress) {
		try {
			myInputStream = AudioSystem.getAudioInputStream(new File (adress));
			myAudioFormat = myInputStream.getFormat();
			int groesse = (int) (myAudioFormat.getFrameSize() * myInputStream.getFrameLength());
			byte [] mySound = new byte[groesse];
			DataLine.Info myInfo = new DataLine.Info(Clip.class, myAudioFormat, groesse);
			myInputStream.read(mySound, 0, groesse);
			myClip = (Clip) AudioSystem.getLine(myInfo);
			myClip.open(myAudioFormat, mySound, 0, groesse);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		return myClip;
	}

	//spielt den Clip ab
	public void playSound(Type type){
		stopSound(type);
		clips[type.getCode()].start();}


	public  void stopSound(Type type){
		Clip clip = clips[type.getCode()];
		clip.stop();
		//setzt den Sound auf den anfang zurück
		clip.flush();
		clip.setFramePosition(0);
	}

	public void loopSound(Type type, int count){
		stopSound(type);
		clips[type.getCode()].loop(count);
	}

	public void stopAllSounds(){
		for(Type type : EnumSet.allOf(Type.class)){
			stopSound(type);
		}
	}

	public void setSound(){
		stopAllSounds();
		if(houston.currentCard == "GAME"){
			loopSound(Sounds.Type.GAME, 100);
		}else{
			loopSound(Sounds.Type.MAIN_MENU, 100);
		}
	}
}

