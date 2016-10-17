package net.anei.cadpage.parsers;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Eventually this will be a full fledged smart address parser wrapper.
 * Right now is largely exists to prove that we have all the necessary class
 * references revolved.  And secondarily to document what all can be done
 * with it
 */
public class SAPWrapper extends SmartAddressParser {
  
  public static void main(String[] args) {
    
    System.out.println("We made it!!!");
    
    if (args.length == 0) return;
    
    // a lot of setup work has to be done before we can call any of the 
    // class constructors, so we might as well do it all here.
    
    // Set up default city, state, and country code
    String defCity = "BENTON COUNTY";
    String defState = "OR";
    CountryCode ccode = CountryCode.US;
    
    // Now we want to invoke one of three constructors, depending on whether we have 
    // a city list or city code table or none of the above
    SAPWrapper parser;
    if (args[0].equals("CITYLIST")) {
      String[] cityList = new String[]{"PHILOMATH", "CORVALLIS", "MONROE"};
      parser = new SAPWrapper(cityList, defCity, defState, ccode);
    }
    else if (args[0].equals("CITYCODES")) {
      Properties cityCodes = buildCodeTable(new String[]{
          "PH", "PHILOMATH",
          "CO", "CORVALLIS",
          "MO", "MONROE"
      });
      parser = new SAPWrapper(cityCodes, defCity, defState, ccode);
    }
    else {
      parser = new SAPWrapper(defCity, defState, ccode);
    }
    
    // OK, we now have a parser.  Now we can call all kinds of optional setup methods
    // None of these are required, most are only occasionally used.
    // Important note.  Everything passed to one of these methods must be upper case.
    // The actual tests are case insensitive, but they will fail if anythings
    // is passed containing lower case charcaters.
    
    // One of the more common setup routines.  This is called if the CAD system
    // generally generates a restricted set of call descriptions.  This is a huge
    // help if a call description preceeds the address.  It does not have to be
    // complete, if we do not find a match here we just fall back and use the 
    // regular address parsing logic
    if (args[0].equals("CALL_LIST")) {
      CodeSet callSet = new CodeSet("FIRE", "EMS", "REALLY BAD NEWS");
      parser.setupCallList(callSet);
    }
    
    // This defines the list of known multi-word street names.  It comes into 
    // play when something is found in front of a naked street name or intersection.
    // without it, we have know way to know how many words should belong to the
    // street name and will assume there is only one.  This list generally 
    // is consulted only when the street name if followed by a recognized street suffix 
    // (RD, AVE, ST, etc) but the street suffix should not be included in the 
    // street name passed to this method.
    if (args[0].equals("MULTIWORD_STREETS")) {
      String[] mwordStreets = new String[]{"MARTIN LUTHER KING", "JAMES TAYLOR"};
      parser.setupMultiWordStreets(mwordStreets);
    }
    
    // This defines a list of known special street names.  These are street names that are
    // complete in and of themselves.  Without a trailing street suffix word.
    if (args[0].equals("SPECIAL_STREETS")) {
      String[] mwordStreets = new String[]{"PARKWAY", "INDIANAPOLIS SPEEDWAY"};
      parser.setupSpecialStreets(mwordStreets);
    }
    
    // Normally NORTH, SOUTH, EAST, and WEST are not considered actual directions that can be 
    // used to qualify street names like N, S, E and W.  Calling this adds them to the valid
    // directions list
    if (args[0].equals("ADD_EXTENDED_DIRECTIONS")) {
      parser.addExtendedDirections();
    }
    
    // Most state/county roads are numbered, like CO 270.  Some weird
    // districts have alpha road names, like CO A or CO AA.  But only
    // very exceptional districts use N, S, E, or W as a road name.
    // Parser for those districts have to set this flag
    if (args[0].equals("ALLOW_DIRECTION_HWY_NAMES")) {
      parser.setAllowDirectionHwyNames(true);
    }
    
    // If the district navigation is primarily nautical, this adds several 
    // nautical terms to what would normally be the road suffix list
    if (args[0].equals("ADD_NAUTICAL_TERMS")) {
      parser.addNauticalTerms();
    }
    
    // A more generalized version of the above, this lets you add any terms you
    // want to the road suffix list
    if (args[0].equals("ADD_ROAD_SUFFIX_WORDS")) {
      String[] words = new String[]{"BLK", "BOX", "CRUN"};
      parser.addRoadSuffixTerms(words);
    }
    
    // Used to define words that are not permitted to appear in an address
    if (args[0].equals("ADD_INVALID_WORDS")) {
      String[] badWords = new String[]{"PITCHPIPE"};
      parser.addInvalidWords(badWords);
    }
    
    // There are some special names that are recognized as cross street
    // names that are never valid as real street names.  RR, CITY LIMITS, DEAD END
    // for example.  This lets you add additional names to that list
    if (args[0].equals("ADD_CROSS_STREET_NAMES")) {
      String[] crossStreets = new String[]{"BLUE RIVER"};
      parser.addCrossStreetNames(crossStreets);
    }
    
    // This one is complicated.  When we are looking for a city name to terminate
    // an address, we usually skip over city names followed by a road suffix.  Thus
    // we will not prematurely terminate an address like 235 PHILOMATH BLVD.  A
    // problem arises when a legitimate address is terminated by a legitimate 
    // city name is followed by a business name described as a doctors office, as in
    // 123 BLACK ST PHILOMATH DR MARTINS OFFICE.  To resolve this, you can define a
    // list of known doctors names.  If any of these are found following DR following
    // a city name, the city name will recognized as an address terminator.  A defined
    // name of  MARTIN will protect DR MARTIN or DR MARTINS OFFICE or DR MARTIN'S OFFICE
    if (args[0].equals("DOCTOR_NAMES")) {
      String[] names = new String[]{"BYRAM", "MARTIN"};
      parser.setupDoctorNames(names);
    }
    
    // Same as above, except it protects business names starting with ST
    if (args[0].equals("SAINT_NAMES")) {
      String[] names = new String[]{"MARY", "JOSEPH"};
      parser.setupSaintNames(names);
    }
    
    // Used to remove terms from the primary parser dictionary.  This could be
    // used if some words that are normally considered to mean something are 
    // instead used as a normal part of a street name.
    if (args[0].equals("REMOVE_WORDS")) {
      String[] goodWords = new String[]{"ALLEY"};
      parser.removeWords(goodWords);
    }
    
    // The characters ()[], are not normally allowed to occur inside an address
    // but in at least one case there were necessary, so we add an option to allow them
    if (args[0].equals("ALLOW_BAD_CHARS")) {
      parser.allowBadChars(",");
    }
    
    // On rare occasions, a pattern match is the better way to identify leading place names
    if (args[0].equals("PLACE_PATTERN")) {
      parser.setupPlaceAddressPtn(Pattern.compile(".*? SUBDIVISION"), true);
    }
    
    
    // That is it for setup.  Now we go into a loop to do the actual parsing
    
    while (true) {
      
      // Several things can be done here.  The most common is to parse information
      // from an address line that may, or may not, have something in front of or behind
      // the actual address.  For this we need to define the data object where all of the
      // parsed information will be stored.
      Data data = new Data(parser);
      
      if (args[0].equals("PARSE")) {
        parser.parseAddress(StartType.START_OTHER, FLAG_START_FLD_REQ, "SAVE MY BABY 123 91ST ST KEN IS SPECIAL", data);
        System.out.println(parser.getStart());
        System.out.println(parser.getLeft());
        
        // OK, lets explains some of this
        // StartType tells you what is in front of the address.  or START_ADDR if the field starts with the address.
        // The options are START_ADDR, START_CALL, START_CALL_PLACE, START_PLACE, START_OTHER.  For START_OTHER you 
        // need to call getStart() to retrieve the leading information.  For your purposes, it doesn't make a
        // lot of difference what is passed other than START_ADDR.  Unless a call list has been defined, in which
        // case START_CALL will use that address list to identify the call description.  And START_CALL_PLACE will
        // look for a call description followed by a business name followed by the address.
        
        // There are so many flags doing so many things.  See the SmartAdddressParser logic for an explanation of all
        // of them.  FLAG_START_FLD_REQ is pretty commonly used when there is a leading call description.  It requires
        // that the starting field, whatever it is, cannot be empty.  FLAG_ANCHOR_END the the address/city field must
        // go all the way to the end of the line.
        
        // When you are done, you can just report the information out of the Data object.  In real life it would
        // be converted to a MsgInfo object which would be returned to the caller. as in 
        MsgInfo info = new MsgInfo(data);
        System.out.println(info.getAddress());
        
        // Other information about the results can be retrieved
        parser.getStatus();   // returns integer status of how good an address this is
        parser.getPadField(); //  Field between address and city (you have pass pass a pad field flag to get this)
        parser.isMBlankLeft(); // true if there were multiple blanks between the address and the getLeft() result
        parser.isCommaLeft(); // true if there was a comma between the address and the getLeft() result
      }
      
      // If you have a data field and just what to see if it is valid address, there is a simple check for that
      // This one returns a yes/non value.
      if (args[0].equals("CHECK")) {
        System.out.println(parser.isValidAddress("BAD ADDRESS"));
        
        // Slightly more complicated this returns a integer that can be used to compare the status of two
        // potential address fields.  The higher the number, the better the address
        System.out.println(parser.checkAddress("HOW ABOUT THIS ONE"));
        
        // Or you can just check to see if something is a defined city or not
        System.out.println(parser.isCity("PHILOMATH"));
      }
      
      // When life gets really complicated and you have two or more data fields that might or might not be
      // addresses and you do not want to commit to parsing one of them until you have checked the others.
      // do something like
      if (args[0].equals("ITS_COMPLICATED")) {
        String field1 = "THIS ISNOT REALL AN ADDRESS";
        String field2 = "123 MY OWN ST";
        Result res1 = parser.parseAddress(StartType.START_ADDR, 0, field1);
        Result res2 = parser.parseAddress(StartType.START_ADDR, 0, field2);
        if (res1.getStatus() > res2.getStatus()) {
          res1.getData(data);
        } else {
          res2.getData(data);
        }
      }
      
      // I didn't think of this till know, but you could use this library to
      // implement Cadpage map address adjustments.  Whether you should or not
      // is another question.  My adjustment logic has evolved over several years
      // and in some cases fixes issues that Google has long since addressed itself
      if (args[0].equals("ADJUST_MAP_ADDR")) {
        parser.parseAddress("1300 US ST HWY 20 WO KINGS VALLEY HWY", data);
        data.strCross = "KINGS VALLEY HWY";
        MsgInfo info = new MsgInfo(data);
        System.out.println(info.getMapAddress(false, null, null));
      }
      
      // We really do not want to do this forever do we
      break;
    }
  }
  
  private SAPWrapper(String defCity, String defState, CountryCode ccode) {
    super(defCity, defState, ccode);
  }
  
  private SAPWrapper(String cityList[], String defCity, String defState, CountryCode ccode) {
    super(cityList, defCity, defState, ccode);
  }
  
  private SAPWrapper(Properties cityCodes, String defCity, String defState, CountryCode ccode) {
    super(cityCodes, defCity, defState, ccode);
  }
}
