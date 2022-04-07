package Streamer;

import java.io.*;

public class VideoStream {
  FileInputStream fis;
  int imagenb; // image nb of the image currently transmitted
  byte[] sBuf;
  String filename;

  static int VIDEO_LENGTH = 500; // length of the video in frames

  public VideoStream(String filename) throws Exception {

    // init variables
    this.filename = filename;
    fis = new FileInputStream(filename);
    imagenb = 0;
    sBuf = new byte[15000];
  }

  // -----------------------------------
  // getnextframe
  // returns the next frame as an array of byte and the size of the frame
  // -----------------------------------
  public int getnextframe(byte[] frame) throws Exception {
    int length = 0;
    String length_string;
    byte[] frame_length = new byte[5];

    // read current frame length
    fis.read(frame_length, 0, 5);

    // transform frame_length to integer
    length_string = new String(frame_length);
    length = Integer.parseInt(length_string);

    return (fis.read(frame, 0, length));
  }

  public void nextImage() throws IOException {
    imagenb++;
    if (imagenb >= VIDEO_LENGTH) {
      imagenb = 0;
      fis.close();
      fis = new FileInputStream(filename); 
    }
  
  }

  public byte[] actionPerformed() throws IOException {
    byte[] data = null;
    
    if (imagenb < VIDEO_LENGTH) {
      // update current imagenb
      nextImage();
      try {
        // get next frame to send from the video, as well as its size
        int image_length = getnextframe(sBuf);
        data = new byte[image_length];
        for (int i = 0; i < image_length; i++)
          data[i] = sBuf[i];
      } 
      catch (Exception ex) {
        System.out.println("Exception caught: " + ex);
        System.exit(0);
      }
    }
    else {
      // if we have reached the end of the video file, stop the timer
      return null;
    }
    return data;
  }
}
