package brostore.maquillage.manager;

import android.os.Environment;
import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CacheManager {

	private static final String STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath();
	private static final String TAG = "CacheManager";

	public static JSONObject loadCache( String directory, String filename ) {
		File fIn = null;
		FileInputStream istream = null;
		try {
			fIn = new File(STORAGE_DIRECTORY + "/" + directory + "/" + filename);
			// Read file into byte array
			istream = new FileInputStream(fIn);

			// Read file into byte array
			byte[] dataWritten = new byte[(int) fIn.length()];
			BufferedInputStream bistream = new BufferedInputStream(istream);
			bistream.read(dataWritten);
			bistream.close();

			// Create parcel with cached data
			Parcel parcelIn = Parcel.obtain();
			parcelIn.unmarshall(dataWritten, 0, dataWritten.length);
			parcelIn.setDataPosition(0);
			return new JSONObject(parcelIn.readString().toString());
		} catch (FileNotFoundException e) {
			Log.e( TAG, "FileNotFoundException : CacheManager : " + e.getMessage());
		} catch (IOException e) {
			Log.e( TAG, "IOException : CacheManager : " + e.getMessage());
		} catch (JSONException e) {
			Log.e( TAG, "JSONException : CacheManager : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e( TAG, "NullPointerException : CacheManager : " + e.getMessage());
		}  finally { 
			closeStream(istream); 
		}
		return null;
	}

	public static boolean createCache(JSONObject jsonObject, String directory, String filename ){
		FileOutputStream ostream = null;
		try {
			createDirectoryIfNeeded(STORAGE_DIRECTORY + "/" + directory);
			File cacheMediaFile = new File( STORAGE_DIRECTORY + "/" + directory, filename );
			// Write object into parcel
			Parcel parcelOut = Parcel.obtain();
			parcelOut.writeString(jsonObject.toString());

			// Write byte data to file
			ostream = new FileOutputStream(cacheMediaFile);
			BufferedOutputStream bistream = new BufferedOutputStream(ostream);
			bistream.write(parcelOut.marshall());
			bistream.close();
			parcelOut.recycle();
		} catch (FileNotFoundException e) {
			Log.e( TAG, "FileNotFoundException : saveJson : " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.e( TAG, "IOException : saveJson : " + e.getMessage());
			return false;
		} catch (OutOfMemoryError e) {
			Log.e( TAG, "OutOfMemoryError : saveJson : " + e.getMessage());
			return false;
		} finally { 
			closeStream(ostream);
		}
		return true;
	}

	public static Object loadCacheObject(String directory, String filename) {
		Object object = null;
		InputStream fIn = null;
		ObjectInputStream objIn = null;
		try {
			fIn = new FileInputStream(STORAGE_DIRECTORY + "/" + directory + "/"
					+ filename);
			objIn = new ObjectInputStream(fIn);
			object = objIn.readObject();
			fIn.close();
		} catch (FileNotFoundException e) {
			Log.e(TAG,
					"FileNotFoundException : CacheManager : " + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "IOException : CacheManager : " + e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeStream(objIn);
			closeStream(fIn);
		}
		return object;
	}

	public static void createCacheObject(Object object, String directory, String filename) {
		FileOutputStream out = null;
		ObjectOutputStream objOut = null;
		try {
			createDirectoryIfNeeded(STORAGE_DIRECTORY + "/" + directory);
			File cacheMediaFile = new File(STORAGE_DIRECTORY + "/" + directory,	filename);
			out = new FileOutputStream(cacheMediaFile);
			objOut = new ObjectOutputStream(out);
			objOut.writeObject(object);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "FileNotFoundException : saveJson : " + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "IOException : saveJson : " + e.getMessage());
		} catch (OutOfMemoryError e) {
			Log.e(TAG, "OutOfMemoryError : saveJson : " + e.getMessage());
		} finally {
			closeStream(objOut);
			closeStream(out);
		}
	}

	private static void createDirectoryIfNeeded(String directory) {
		File root = Environment.getExternalStorageDirectory();
		if (!root.canWrite()) {
			Log.e(TAG, "ERROR : SDCARD IS NOT WRITEABLE");
		}

		File path = new File(directory);
		if (!path.isDirectory()) {
			path.mkdirs();
		}
	}

	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				Log.e(TAG, "IOException " + e.getMessage());
			}
		}
	}

	public static void deleteFolderContent(String directory) {
		try {
			createDirectoryIfNeeded(STORAGE_DIRECTORY + "/" + directory);
			File fileOrDirectory = new File(
					STORAGE_DIRECTORY + "/" + directory, "temp.tmp");
			fileOrDirectory = fileOrDirectory.getParentFile();
			DeleteFolderRecursive(fileOrDirectory);
		} catch (Exception e) {

		}
	}

	private static void DeleteFolderRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				DeleteFolderRecursive(child);

		fileOrDirectory.delete();
	}
}