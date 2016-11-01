package mm.smy.bicq ;

/**
* �ṩ������ͷ��Ĵ���
* @date 2003-11-17
* 
* 
* 
* 
* 
*/

import java.io.* ;

import javax.swing.ImageIcon ;
import javax.swing.Icon ;
import java.awt.image.MemoryImageSource ;
import java.awt.Image ;

import mm.smy.bicq.user.User ;

public class FaceManager{
	
	/**
	* ���ָ��ͷ���ţ��ƶ�״̬�� ͷ��ImageIcon������д��󣬷��ص�1�ŵ�ͷ��
	* @param portrait ͷ��ı�ţ����ϵͳû���ҵ�������Ĭ�ϵ�ͷ�񡣵�0�š�
	* @param state Ŀǰ��ͷ����ѵ�״̬��
	* @return ��ø�ͷ���״̬��ImageIcon����
	*/
	public static ImageIcon getFaceIcon(int portrait, int state){
    	if(portrait < 1 ) portrait = 1 ; //��ǰĬ�ϵ���70
    	
    	String path = "face/" + portrait ;
    	
    	if(state == User.ONLINE)
    		path = path + "-1.bmp" ;
    	else if(state == User.LEAVE)
    		path = path + "-3.bmp" ;
    	else
    		path = path + "-2.bmp" ;
    	
 	//	File file = new File(path) ;
    //	System.out.println(file.getAbsolutePath()) ;
   	
    	Image bmp = null ;
    	try{
    		bmp = loadBitmap(path) ;
    	}catch(Exception e){    		
    		System.out.println("face manager:" + e.getMessage() ) ;
    		try{
    			bmp = loadBitmap("face/1-1.bmp") ;	
    		}catch(Exception ee){
    			return null ;	
    		}
    	}
    	return new ImageIcon(bmp) ;
    	
 /*       java.net.URL imgURL = GuestPanel.class.getResource(path) ;
        if (imgURL != null) {
            return new ImageIcon(imgURL) ;
        }else {
            System.err.println("Couldn't find file: " + path) ;
            return null ;
        }		
*/	}
	
	
   /**
 	*��ȡδѹ���� 24 λ�� 8 λͼ��
 	*���ͼ���� 24 λ�� 8 λͼ����Ǳ�ѹ�����׳�IOException
 	@param dir Ҫ����λͼ��·����
	@return Image �������Ƶ�Image����
	@exception FileNotFoundException, IOException
    */	
	public static Image loadBitmap(String dir) throws FileNotFoundException, IOException{
		Image image ;
		//System.out.println("loading:"+sdir+sfile) ;

    	FileInputStream fs=new FileInputStream(dir) ;
    	BufferedInputStream bis = new BufferedInputStream(fs) ;
    	int bflen=14 ; // 14 �ֽ� BITMAPFILEHEADER
    	byte bf[]=new byte[bflen] ;
   		bis.read(bf,0,bflen) ;
    	int bilen=40 ; // 40 �ֽ� BITMAPINFOHEADER
    	byte bi[]=new byte[bilen] ;
    	bis.read(bi,0,bilen) ;

     	// �������ݡ�
    	int nsize = (((int)bf[5]&0xff)<<24) 
  		| (((int)bf[4]&0xff)<<16)
  		| (((int)bf[3]&0xff)<<8)
  		| (int)bf[2]&0xff ;
 		//System.out.println("File type is :"+(char)bf[0]+(char)bf[1]) ;
 		//System.out.println("Size of file is :"+nsize) ;

   		int nbisize = (((int)bi[3]&0xff)<<24)
 		| (((int)bi[2]&0xff)<<16)
 		| (((int)bi[1]&0xff)<<8)
		| (int)bi[0]&0xff ;
 	   // System.out.println("Size of bitmapinfoheader is :"+nbisize) ;

     	int nwidth = (((int)bi[7]&0xff)<<24)
  		| (((int)bi[6]&0xff)<<16)
  		| (((int)bi[5]&0xff)<<8)
  		| (int)bi[4]&0xff ;
    	//System.out.println("Width is :"+nwidth) ;

     	int nheight = (((int)bi[11]&0xff)<<24)
  		| (((int)bi[10]&0xff)<<16)
  		| (((int)bi[9]&0xff)<<8)
  		| (int)bi[8]&0xff ;
    	//System.out.println("Height is :"+nheight) ;

     	int nplanes = (((int)bi[13]&0xff)<<8) | (int)bi[12]&0xff ;
     	//System.out.println("Planes is :"+nplanes) ;

     	int nbitcount = (((int)bi[15]&0xff)<<8) | (int)bi[14]&0xff ;
     	//System.out.println("BitCount is :"+nbitcount) ;

     	// ���ұ���ѹ���ķ���ֵ
     	int ncompression = (((int)bi[19])<<24)
  		| (((int)bi[18])<<16)
 		| (((int)bi[17])<<8)
  		| (int)bi[16] ;
     	//System.out.println("Compression is :"+ncompression) ;

    	int nsizeimage = (((int)bi[23]&0xff)<<24)
  		| (((int)bi[22]&0xff)<<16)
  		| (((int)bi[21]&0xff)<<8)
  		| (int)bi[20]&0xff ;
  		//System.out.println("SizeImage is :"+nsizeimage) ;

     	int nxpm = (((int)bi[27]&0xff)<<24)
  		| (((int)bi[26]&0xff)<<16)
  		| (((int)bi[25]&0xff)<<8)
  		| (int)bi[24]&0xff ;
   		//System.out.println("X-Pixels per meter is :"+nxpm) ;

     	int nypm = (((int)bi[31]&0xff)<<24)
  		| (((int)bi[30]&0xff)<<16)
  		| (((int)bi[29]&0xff)<<8)
  		| (int)bi[28]&0xff ;
     	//System.out.println("Y-Pixels per meter is :"+nypm) ;

  		int nclrused = (((int)bi[35]&0xff)<<24)
 		| (((int)bi[34]&0xff)<<16)
		| (((int)bi[33]&0xff)<<8)
 		| (int)bi[32]&0xff ;
     	//System.out.println("Colors used are :"+nclrused) ;

     	int nclrimp = (((int)bi[39]&0xff)<<24)
  		| (((int)bi[38]&0xff)<<16)
  		| (((int)bi[37]&0xff)<<8)
  		| (int)bi[36]&0xff ;
    	//System.out.println("Colors important are :"+nclrimp) ;

     	if (nbitcount==24) {
  			// 24 λ��ʽ��������ɫ�����ݣ���ɨ���б����㵽
  			// 4 ���ֽڡ�
  			int npad = (nsizeimage / nheight) - nwidth * 3 ;
  			int ndata[] = new int [nheight * nwidth] ;
  			byte brgb[] = new byte [( nwidth + npad) * 3 * nheight] ;
  			bis.read (brgb, 0, (nwidth + npad) * 3 * nheight) ;
  			int nindex = 0 ;
  			for (int j = 0 ; j < nheight ; j++){
      			for (int i = 0 ; i < nwidth ; i++){
   					ndata [nwidth * (nheight - j - 1) + i] =
       				(255&0xff)<<24
       				| (((int)brgb[nindex+2]&0xff)<<16)
       				| (((int)brgb[nindex+1]&0xff)<<8)
       				| (int)brgb[nindex]&0xff ;
    				//System.out.println("Encoded Color at (" +i+","+j+")is:"+brgb+" (R,G,B)= (" +((int)(brgb[2]) & 0xff)+","+((int)brgb[1]&0xff)+"," +((int)brgb[0]&0xff)+")") ;
   					nindex += 3 ;
   				}
      			nindex += npad ;
      		}
  			image = java.awt.Toolkit.getDefaultToolkit().createImage( new MemoryImageSource (nwidth, nheight, ndata, 0, nwidth)) ;
  		}else if (nbitcount == 8){
 			// ����ȷ����ɫ������� clrsused �������� 0��
  			// ����ɫ��������������������� 0�������
 			// bitsperpixel ������ɫ����
  			int nNumColors = 0 ;
  			if (nclrused > 0){
     			nNumColors = nclrused ;
      		}else{
      			nNumColors = (1&0xff)<<nbitcount ;
      		}
  			//System.out.println("The number of Colors is"+nNumColors) ;

  			// ĳЩλͼ������ sizeimage �����ҳ�
  			// ��Щ����������ǽ���������
  			if (nsizeimage == 0){
      			nsizeimage = ((((nwidth*nbitcount)+31) & ~31 ) >> 3) ;
      			nsizeimage *= nheight ;
      			//System.out.println("nsizeimage (backup) is"+nsizeimage) ;
      		}

  			// ��ȡ��ɫ����ɫ��
  			int npalette[] = new int [nNumColors] ;
  			byte bpalette[] = new byte [nNumColors*4] ;
  			bis.read (bpalette, 0, nNumColors*4) ;
  			int nindex8 = 0 ;
  			for (int n = 0 ; n < nNumColors ; n++){
      			npalette[n] = (255&0xff)<<24
   				| (((int)bpalette[nindex8+2]&0xff)<<16)
   				| (((int)bpalette[nindex8+1]&0xff)<<8)
   				| (int)bpalette[nindex8]&0xff ;
       			//System.out.println ("Palette Color "+n
				//+" is:"+npalette[n]+" (res,R,G,B)= ("
   				//+((int)(bpalette[nindex8+3]) & 0xff)+","
   				//+((int)(bpalette[nindex8+2]) & 0xff)+","
   				//+((int)bpalette[nindex8+1]&0xff)+","
   				//+((int)bpalette[nindex8]&0xff)+")") ;
      			nindex8 += 4 ;
      		}

  			// ��ȡͼ�����ݣ�ʵ�����ǵ�ɫ���������
  			// ɨ�����Ա����㵽 4 ���ֽڡ�
  			int npad8 = (nsizeimage / nheight) - nwidth ;
  			//System.out.println("nPad is:"+npad8) ;

  			int ndata8[] = new int [nwidth*nheight] ;
  			byte bdata[] = new byte [(nwidth+npad8)*nheight] ;
  			bis.read (bdata, 0, (nwidth+npad8)*nheight) ;
  			nindex8 = 0 ;
  			for (int j8 = 0 ; j8 < nheight ; j8++){
      			for (int i8 = 0 ; i8 < nwidth ; i8++){
   					ndata8 [nwidth*(nheight-j8-1)+i8] =
       				npalette [((int)bdata[nindex8]&0xff)] ;
   					nindex8++ ;
   				}
      		nindex8 += npad8 ;
     	 }
     	 
  		 image = java.awt.Toolkit.getDefaultToolkit().createImage( new MemoryImageSource (nwidth, nheight, ndata8, 0, nwidth)) ;
  		}else{
  			bis.close() ;
  			fs.close() ;
  			throw new IOException("bmp type not supported.Ŀǰֻ֧��8λ��24λδѹ��λͼ��") ;
  		}

     	bis.close() ;
     	return image ;
	}
	
	
	
	
	
	
	
	
}
