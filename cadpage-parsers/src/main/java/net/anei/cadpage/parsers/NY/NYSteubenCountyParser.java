package net.anei.cadpage.parsers.NY;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Steuben County, NY
 */
public class NYSteubenCountyParser extends SmartAddressParser {
  
  public NYSteubenCountyParser() {
    super(CITY_LIST, "STEUBEN COUNTY", "NY");
    setFieldList("SRC PLACE ADDR APT CITY ST X CODE CALL INFO ID");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com,sc911@centralny.twcbc.com";
  }

  private static final Pattern MARKER = Pattern.compile("^messaging@iamresponding.com \\( *(.*?) *\\) ");
  private static final Pattern SUBJECT_PTN = Pattern.compile(".* FD|.* AMB|SC911");
  
  private static final Pattern[] TRAILER_PTNS = {
    Pattern.compile("(AVOCA?FDA?|AVOCAFDAMB|ATLANFD|BATHAMB|BRADFD|BTFD|COHOFAM|COHOFD|CATOFD|HAMFD|HOWAFD|KANOFD|TUSCAFD|WAYLA|WAYNE?FD|FD):([:\\d]*)$"),
    Pattern.compile("([A-Z]+):([:\\d]*)$"),
    Pattern.compile("\n([A-Z]+)()$")
  };
  
  private static final Pattern INFO_HEADER = Pattern.compile("^--?\\{\\[?(.*?)\\]?\\}--?");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<![ 0-9])(?=\\d+[A-Z]\\d+[A-Z]? |FD )");
  private static final Pattern MASTER1 = Pattern.compile("/ *(.*?) \\((.*?)\\)(?:, +([ A-Z]+) (?:TOWN|VILLAGE)(?: OF)?)?/?(.*)");
  private static final Pattern PAREN_PTN = Pattern.compile("\\((?!CVA)([^a-z]*?|.*; Near:.*)\\)");
  private static final Pattern MASTER6 = Pattern.compile("(.*?) T/(NELSON)(.*)");
  
  private static final Pattern CITY_PTN1 = Pattern.compile(" (?:TOWN|VILLAGE) OF ([ A-Z]+) ; ");
  private static final Pattern CITY_PTN2 = Pattern.compile("(?:, ([ A-Z]+))?(?: (?:TOWN|VILLAGE)|(?<= DUNDEE)(?<! IN DUNDEE) )(?! RD\\b)(?: OF)?/?");
  private static final Pattern CODE_CALL_PTN = Pattern.compile("\\b(?:(\\d{1,2}[A-Z](?:\\d{1,2}[A-Z]?| \\d+-\\d+))[- ]+|(?=FD ))");
  
  
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) { 

    // There are a couple different text signatures we look for
    boolean sigMatch = true;
    Matcher match;
    do {
	    // Look for starting page signature
	    match = MARKER.matcher(body);
	    if (match.find()) {
	      data.strSource = match.group(1);
	      body = body.substring(match.end()).trim();
	      break;
	    }
	    
	    // Sometimes the starting sender address is missing, which means the
	    // station code ends up in the description
	    if (SUBJECT_PTN.matcher(subject).matches()) {
	      data.strSource = subject;
	      break;
	    }
	    
	    // Nothing worked, we will keep trying, but will have a much stricter 
	    // standard of what constitutes a CAD page
	    sigMatch = false;
    } while (false);

    // If we found a start signature match, which included a station source
    // Look for and remove any trailing station and call ID
    boolean found = false;
    for (Pattern ptn : TRAILER_PTNS) {
      match = ptn.matcher(body);
      found = match.find();
      if (found) break;
    }
    if (found) {
      if (data.strSource.length() == 0) data.strSource = match.group(1);
      data.strCallId = match.group(2);
      body = body.substring(0,match.start()).trim();
    }
    
    body = body.replace('\n', ' ').replace('\\', '/');
    
    // Check for special information header
    match = INFO_HEADER.matcher(body);
    if (match.find()) {
      data.strCall = match.group(1) + " - ";
      body = body.substring(match.end()).trim();
    }
    
    // Check for RECALLED tag
    else if (body.startsWith("::::RECALLED:::::: ")) {
      data.strCall = "RECALLED - ";
      body = body.substring(19).trim();
    } else if (subject.equals("2ndCall") || subject.equals("2nd Call")) {
      data.strCall = subject + " - ";
    }
    
    body = body.replace("COUNTY ROUTE", "COUNTY ROAD");
    body = MISSING_BLANK_PTN.matcher(body).replaceFirst(" ");
    
    // There a couple basic patterns
    // /<place> (<addr> <city> [cross]) [code] <call>
    // /<place> (<addr>), <city> [code] <call>
    String info;
    if ((match = MASTER1.matcher(body)).matches()) {
      data.strPlace = match.group(1).trim();
      String addr = match.group(2);
      data.strCity = getOptGroup(match.group(3));
      info = match.group(4).trim();
      
      if (data.strCity.length() > 0) {
        parseAddress(addr, data);
      } else {
        parseAddress(StartType.START_ADDR, addr, data);
        data.strCross = cleanCross(cleanLeft(getLeft()));
      }
    }
    
    // for all othe patterns, parens can occur anywhere and
    // always enclose cross streets
    else {
      while ((match = PAREN_PTN.matcher(body)).find()) {
        String cross = match.group(1).trim();
        body = body.substring(0,match.start()).trim() + " " + body.substring(match.end()).trim();
        body = body.trim();
        
        int pt = cross.indexOf("; Near:");
        if (pt >= 0) {
          data.strPlace = append(data.strPlace, " - ", cross.substring(pt+2));
          cross = cross.substring(0,pt).trim();
        }
        data.strCross = append(data.strCross, ", ", cleanCross(cross));
      }
      
      // <addr> TOWN/VILLAGE OF <city> ; <info>
      if ((match = CITY_PTN1.matcher(body)).find()) {
        parseAddress(body.substring(0,match.start()).trim(), data);
        data.strCity = match.group(1).trim();
        info = body.substring(match.end()).trim();
      }
      
      // <addr>, <city> <info>
      // <addr> <city> <info>
      else if ((match = CITY_PTN2.matcher(body)).find()) {
        String addr = body.substring(0,match.start()).trim();
        String city = match.group(1);
        info = body.substring(match.end()).trim();
        if (city != null) {
          data.strCity = city.trim();
          parseAddress(addr, data);
        } else {
          parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);
        }
      }
      
      // <addr> T/<city> <info>
      else if ((match = MASTER6.matcher(body)).matches()) {
        parseAddress(match.group(1).trim(), data);
        data.strCity = match.group(2);
        info = match.group(3).trim();
      }
      
      // Getting desperate, but see if we can find a code/call signature
      else if ((match = CODE_CALL_PTN.matcher(body)).find()) {
        int pt = match.start();
        String addr = body.substring(0,pt).trim();
        info = body.substring(pt);
        Result res = parseAddress(StartType.START_ADDR, FLAG_IGNORE_AT, addr);
        if (res.isValid()) {
          res.getData(data);
          addr = res.getLeft();
        } 
        data.strSupp = append(data.strSupp, " / ", addr);
      }
      
      // Anything else is just a info
      // If we didn't find a matching start signature and didn't find a
      // master pattern match, the we had better conclude that this is not
      // a real CAD page
      else {
        if (!sigMatch) return false;
        data.strSupp = append(data.strSupp, " / ", body);
        info = "";
      }
    }
    
    // See if there is another city in the rest of the information, even
    // if we have already found one city
    match = CITY_PTN2.matcher(info);
    if (match.find()) {
      String part = info.substring(0,match.start()).trim();
      String city = match.group(1);
      info = info.substring(match.end()).trim();
      if (city != null) {
        data.strCity = city;
      } else {
        parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, part, data);
        part = getStart();
      }
      if (data.strCross.length() == 0 && part.contains("/")) {
        data.strCross = cleanCross(part);
      } else {
        data.strSupp = append(data.strSupp, " / ", part);
      }
    }
    
    // See if there is a code in what we picked out as info
    match = CODE_CALL_PTN.matcher(info);
    if (match.find()) {
      data.strPlace = append(data.strPlace, " - ", info.substring(0,match.start()).trim());
      data.strCode = getOptGroup(match.group(1));
      info = info.substring(match.end());
    }
    
    String call = null;
    if (info.length() > 0) {
      if (info.length() <= 40) {
        call = info;
        info = "";
      } else {
        int pt = info.indexOf(" - ");
        if (pt > 0 && pt <= 40) {
          call = info.substring(0,pt).trim();
          info = info.substring(pt+3).trim();
        }
      }
    }
    if (call == null) call = "ALERT";
    data.strCall = data.strCall + call;
    data.strSupp = append(data.strSupp, " / ", info);
    
    if (PA_CITIES.contains(data.strCity.toUpperCase())) data.strState = "PA";
    
    return true;
  }

  protected String cleanLeft(String left) {
    if (left.startsWith("TOWN OF")) left = left.substring(7).trim();
    else if (left.startsWith("VILLAGE OF")) left = left.substring(10).trim();
    return left;
  }

  private String cleanCross(String field) {
    field = stripFieldStart(field, "/");
    field = stripFieldEnd(field, "/");
    field = stripFieldEnd(field, ";");
    return field;
  }
  
  private static final String[] CITY_LIST = new String[]{
    "ADDISON",
    "ALMOND",
    "ARKPORT",
    "AVOCA",
    "BATH",
    "BRADFORD",
    "CAMERON",
    "CAMPBELL",
    "CANISTEO",
    "CANISTEO",
    "CATON",
    "COHOCTON",
    "CORNING",
    "DANSVILLE",
    "ERWIN",
    "FREMONT",
    "GANG MILLS",
    "GREENWOOD",
    "HAMMONDSPORT",
    "HARTSVILLE",
    "HORNBY",
    "HORNELL",
    "HORNELLSVILLE",
    "HOWARD",
    "JASPER",
    "LINDLEY",
    "NORTH HORNELL",
    "PAINTED POST",
    "PRATTSBURGH",
    "PULTENEY",
    "RATHBONE",
    "RIVERSIDE",
    "STEPHENS MILLS",
    "SAVONA",
    "SOUTH CORNING",
    "THURSTON",
    "TROUPSBURG",
    "TUSCARORA",
    "URBANA",
    "WAYLAND",
    "WAYNE",
    "WEST UNION",
    "WHEELER",
    "WOODHULL",
    
    // Yates County
    "DUNDEE",
    
    // Tiaoga County
    "DEERFIELD"
  };
  
  private static final Set<String> PA_CITIES = new HashSet<String>(Arrays.asList(new String[]{
      "DEERFIELD",
      "NELSON"
  }));
}
	