/*******************************************************************************
**
** Advanced Distributed Learning Co-Laboratory (ADL Co-Lab) grants you 
** ("Licensee") a non-exclusive, royalty free, license to use, modify and 
** redistribute this software in source and binary code form, provided that 
** i) this copyright notice and license appear on all copies of the software; 
** and ii) Licensee does not utilize the software in a manner which is 
** disparaging to ADL Co-Lab.
**
** This software is provided "AS IS," without a warranty of any kind.  ALL 
** EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING 
** ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE 
** OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED.  ADL Co-Lab AND ITS LICENSORS 
** SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF 
** USING, MODIFYING OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES.  IN NO 
** EVENT WILL ADL Co-Lab OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, 
** PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
** INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE 
** THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE 
** SOFTWARE, EVEN IF ADL Co-Lab HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH 
** DAMAGES.
**
*******************************************************************************/
package com.ziaan.scorm2004.util.zip;

//Native java imports
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 *
 * <strong>Filename:</strong><br>
 * UnZipHandler.java<br><br>
 *
 * <strong>Description:</strong><br>
 * A <CODE>UnZipHandler</CODE> provides the ability to extract the contents
 * of a zipped file to a given directory.<br><br>
 *
 *
 * <strong>Design Issues:</strong> None<br>
 * <br>
 *
 * <strong>Implementation Issues:</strong> None<br><br>
 *
 * <strong>Known Problems:</strong> None<br><br>
 *
 * <strong>Side Effects:</strong> The files will be extracted to the given
 * directory<br><br>
 *
 * <strong>References:</strong> None<br><br>
 *
 * @author ADL Technical Team
 */
public class UnZipHandler
{
   /**
    * zip file to extract
    */
   private File mZipFile;

   /**
    * Extract directory
    */
   private String mExtractToDir;

   /**
    * Constructor for the <code>UnZipHandler</code> class.<br>
    *
    * @param zipFileName
    *               Name and path of the <code>.zip</code> file.<br>
    * @param targetDirName
    *               Name and path of the directory to extract the contents of
    *               the zip file to.<br>
    */
   public UnZipHandler(String iZipFileName, String iTargetDirName )
   {
      setFile( iZipFileName );
      setTargetDirectory( iTargetDirName );
   }

   /**
    * Set the name of the zip file to be extracted.<br>
    *
    * @param iFileName Name and path of the zip file to be extracted.<br>
    */
   private void setFile(String iFileName)
   {
      try
      {
         mZipFile = new File(iFileName);
      }
      catch ( NullPointerException npe )
      {
         npe.printStackTrace();
      }
   }

   /**
    * Set the target directory of the extracted contents of the zip file.<br>
    *
    * @param iTargetDirPath
    *           target directory of the extracted contents of the zip file.<br>
    *
    */
   private void setTargetDirectory( String iTargetDirPath )
   {
      try
      {
         File dir = new File( iTargetDirPath );
         mExtractToDir = iTargetDirPath;
      }
      catch ( NullPointerException npe )
      {
         npe.printStackTrace();
      }
   }

   /**
    * Get the target directory of the extracted contents of the zip file.<br>
    *
    * @return target directory of the extracted contents of the zip file.<br>
    */
   public String getTargetDirectory()
   {
      return mExtractToDir;
   }


   /**
    * Used as a debugging tool.  Writes the contents of a zip file to the
    * dos console.<br>
    *
    * @param zipFileName  --  Name of the zip file to be used<br>
    *
    * @return void<br>
    *
    */
   public void display(ZipFile iTheZipFile)
   {
      System.out.println("****************************");
      System.out.println("in UnZipHandler::display()");
      System.out.println("**************************\n");

      System.out.println("*****************************************");
      System.out.println("The Package Contains the following files:");
      System.out.println("*****************************************\n");


      for (Enumeration entries = iTheZipFile.entries(); entries.hasMoreElements();)
      {
        System.out.println(((ZipEntry)entries.nextElement()).getName());
      }

      System.out.println("\n\n");
      System.out.println("leaving display()");
   }



   /**
    * Locates the file and extracts into the '.' directory.<br>
    *
    * @return void
    *
    */
   public void extract()
   {
      String fileName = new String();
      String destFileName = new String();

      //do our own buffering; reuse the same buffer
      byte[] buffer = new byte[16384];

      try
      {
         ZipFile archive = new ZipFile( mZipFile );

         for ( Enumeration e = archive.entries(); e.hasMoreElements(); )
         {
            //get the next entry in the archive
            ZipEntry entry = (ZipEntry)e.nextElement();

            if ( ! entry.isDirectory() )
            {
               fileName = entry.getName();
               fileName = fileName.replace('/', File.separatorChar);

               destFileName = mExtractToDir + fileName;

               File destFile = new File(destFileName);

               //create the destination path, if needed
               String parent = destFile.getParent();
               if ( parent != null )
               {
                  File parentFile = new File(parent);
                  if ( ! parentFile.exists() )
                  {
                     //create the chain of subdirs to the file
                     parentFile.mkdirs();
                  }
               }

               //get a stream of the archive entry's bytes
               InputStream in = archive.getInputStream(entry);

               //open a stream to the destination file
               OutputStream out = new FileOutputStream(destFileName);

               //Repeat reading into buffer and writing buffer to file,
               //until done.  Count will always be # bytes read, until
               //EOF when it is -1.
               int count;
               while ( (count = in.read(buffer)) != -1 )
               {
                  out.write(buffer, 0, count );
               }

               in.close();
               out.close();
            }
         }
      }
      catch ( ZipException ze )
      {
         ze.printStackTrace();
      }
      catch ( NullPointerException npe )
      {
         npe.printStackTrace();
      }
      catch ( IOException ioe )
      {
         ioe.printStackTrace();
      }
      catch ( SecurityException se )
      {
         se.printStackTrace();
      }
   }
}
