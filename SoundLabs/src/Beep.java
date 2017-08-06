import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Beep implements Runnable{
	ArrayList<Double> freq, xShift, volume;
	int len;
	public Beep(ArrayList<Double> freq, ArrayList<Double> xShift, ArrayList<Double> volume, int len){
		this.freq = freq;
		this.xShift = xShift;
		this.volume = volume;
		this.len = len;
		(new Thread(this)).start();
	}
	
	@Override
	public void run() {
        try {
			tones(freq, xShift, volume, len, 8000f);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * @author Real's HowTo
	 */
	public void tones(ArrayList<Double> hz, ArrayList<Double> x, ArrayList<Double> vol, int msecs, float sampleRate) throws LineUnavailableException 
    {
        byte[] buf = new byte[1];
        AudioFormat af = new AudioFormat(sampleRate,(int)((sampleRate) / 1000),1,true,false);     
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        for (int i=0; i < msecs* (sampleRate) / 1000; i++) {
        	double sum = 0;
        	for(int j = 0; j < hz.size(); ++j) {
        		double angle = (i / (sampleRate / hz.get(j)) * 2.0 * Math.PI);
        		sum += Math.sin(angle + x.get(j) * 343.0) * vol.get(j);
        	}
              buf[0] = (byte)(sum * 127.0);
              sdl.write(buf,0,1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }
}
