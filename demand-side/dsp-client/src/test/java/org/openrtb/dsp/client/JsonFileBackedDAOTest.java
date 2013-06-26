package org.openrtb.dsp.client;

import static org.junit.Assert.assertTrue;


import java.io.FileNotFoundException;
import java.net.URL;
import java.util.concurrent.ConcurrentMap;

import org.junit.Test;
import org.openrtb.dsp.intf.model.DSPException;
import org.openrtb.dsp.intf.model.RTBAdvertiser;
import org.openrtb.dsp.intf.model.RTBExchange;

/*
 * The purpose of JsonFileBackedDAOTest is to Validate JsonFileBackedDAO Class for the following purpose
 * validate configuration file present in the project.
 * validate Json File format.
 * validate loadData() for successfully data uploaded.
 * validate loadData() for Concurrency Test.
 */
public class JsonFileBackedDAOTest
{
    JsonFileBackedDAO dao = null;
    String jsonFile = "/properties.json";
    String incorrectFormatProperties = "/incorrectFormatProperties.json";
    String jsonFileForConcurrencyTest = "/jsonFileForConcurrencyTest.json";

    public JsonFileBackedDAOTest() throws FileNotFoundException
    {
        dao = new JsonFileBackedDAO();
    }

    /**
   	 * This method is used to Validate Json configuration file present or not in the Project.
   	 */
    @Test
    public void fileNotFoundLoadData() throws DSPException
    {
        try
        {
            dao.loadData("dummyfile.json");
        }
        catch (Exception e)
        {
            assertTrue("The DSPException should occur", e.getClass().getName().equals("org.openrtb.dsp.intf.model.DSPException"));
            String msg = "dummyfile.json (The system cannot find the file specified)";
            assertTrue("The message should match", e.getMessage().equals(msg));
        }
    }

    // Validate configuration file is in proper format or not.
    @Test
    public void fileFormatTest() throws DSPException
    {
    	URL url = null;
        try
        {
            // create an incorrect format file
            url = this.getClass().getResource(incorrectFormatProperties);
            dao.loadData(url.getPath());
        }
        catch (DSPException e)
        {
        	String msg = "Can not deserialize instance of java.util.ArrayList out of START_OBJECT token";
            assertTrue("The message should match", e.getMessage().contains(msg));
        }
    }

    /**
	 * This method is used to test the loadData property for Successful data Upload to the Configuration File
	 */
    @Test
    public void testLoadData() throws DSPException
    {
        URL url = this.getClass().getResource(jsonFile);
        dao.loadData(url.getPath());
        ConcurrentMap<String, RTBAdvertiser> advertisers = dao.getAdvertisers();
        assertTrue("expected size of advertisers is", advertisers.size() == 1);
        ConcurrentMap<String, RTBExchange> exchanges = dao.getExchanges();
        assertTrue("expected size of exchange is", exchanges.size() == 1);
    }

    /**
	 * This method test the Validate loadData property for Concurrency Test .
	 */
    @Test
    public void concurrencyLoadDataTest() throws DSPException
    {
        URL url = getClass().getResource(jsonFile);
        dao.loadData(url.getPath());
        ConcurrencyTest concurrencyTest = new ConcurrencyTest();
        new Thread(concurrencyTest).start();
    }

  /**
   * purpose of ConcurrencyTest Class to check Concurrency issues in load() of JsonFileBackedDAO
   **/
    class ConcurrencyTest implements Runnable
    {
        public void run()
        {
            try
            {
                URL url = getClass().getResource(jsonFileForConcurrencyTest);
        		dao.loadData(url.getPath());
                ConcurrentMap<String, RTBAdvertiser> advertisers = dao.getAdvertisers();
                assertTrue("expected size of advertisers is ", advertisers.size() == 2);
            }
            catch (DSPException e)
            {
                e.printStackTrace();
            }

        }
    }
}
