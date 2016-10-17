package net.anei.cadpage.parsers.OK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class OKBryanCountyParser extends DispatchB2Parser {
  
  private static final Pattern PTN_CODE = Pattern.compile("0:[-/A-Z]+ ?>");
  
  public OKBryanCountyParser() {
    super("0:", CITY_LIST, "BRYAN COUNTY", "OK");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "@durant.org,lakewood1051@yahoo.com,15803801917@tmomail.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Replace 911-CENTER prefix with 0 prefix
    if(body.startsWith("911-CENTER:")) {
      body = "0" + body.substring(10);
    }
    
    // Find the code.  If there isn't a '>' following it, then insert one
    if (!body.startsWith("0:EVENT:")) {
      Matcher codeMatch = PTN_CODE.matcher(body);
      if(!codeMatch.find()) {
        int firstSpace = body.indexOf(' ');
        if(firstSpace >= 0) {
          body = body.substring(0, firstSpace+1) + ">" + body.substring(firstSpace+1);
        }
      }
    }
    
    return super.parseMsg(subject, body, data);
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }
  
  private static final Pattern PTN_STREET = Pattern.compile("^((?:[NSEW] +)?\\b[0-9]+) +(?:AND\\b|[&/])");
  @Override
  public String adjustMapAddress(String sAddress) {
    
    // Found an example where it said "16 AND LOCUST".  On the map there is a
    // 16th Street and if you put in "16TH AND LOCUST", Google finds it.
    Matcher streetMatch = PTN_STREET.matcher(sAddress);
    if(streetMatch.find()) {
      String suffix = "TH";
      String street = streetMatch.group(1);
      int len = street.length();
      int lastDigit = street.charAt(len-1) - '0';
      if (lastDigit >= 1 && lastDigit <= 3) {
        if (len == 1 || street.charAt(len-2) != '1') {
          suffix = new String[]{"ST", "ND", "RD"}[lastDigit-1];
        }
      }
      sAddress = street + suffix + " & " + sAddress.substring(streetMatch.end()).trim();
    }
    
    return sAddress;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "BIG VALLEY",
    "BLACKJACK VILLAGE",
    "FOUR SEASONS",
    "HILLTOP HOLLOW",
    "LONE OAK",
    "MEAD CEMETERY",
    "MEAD CEMETREY",
    "ROCKY TOP",
    "TANDY WALKER",
    "THREE MILE"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "ASSAULT",
      "BRUSH FIRE",
      "DFD PAGER TEST SCHEDULE",
      "DOMESTIC IN PROGRESS",
      "FIRE EXPLOSION",
      "FIRE MISC. UNKNOWN TYPE",
      "FOUND PROPERTY",
      "LIFT ASSIST",
      "MEDICAL CALL",
      "MOTOR VEHICLE COLLISION",
      "MVC - UNK INJ",
      "NON EMERG TRANSPORT",
      "STRUCTURE FIRE",
      "SUICIDE OR ATTEMPTED SUICIDE",
      "SUSPICIOUS ACTIVITY",
      "VEHICLE COLLISION /W INJURIES",
      "WARRANT SERVICE / FELONY"
  );
  
  static final String[] CITY_LIST = new String[]{
    
      "BRYAN COUNTY",
      
      //Cities
      "DURANT",

      //Towns
      "ACHILLE",
      "ARMSTRONG",
      "BENNINGTON",
      "BOKCHITO",
      "CADDO",
      "CALERA",
      "COLBERT",
      "HENDRIX",
      "KEMP",
      "KENEFIC",
      "MEAD",
      "SILO",

      //Census-designated places
      "ALBANY",
      "BLUE",
      "CARTWRIGHT",

      //Unincorporated communities
      "ALLISON",
      "BANTY",
      "BROWN",
      "CADE",
      "COBB",
      "KIERSEY",
      "PLATTER",
      "ROMIA",
      "ROBERTA",
      "UTICA",
      "YARNABY",
      "YUBA"
  };


}
