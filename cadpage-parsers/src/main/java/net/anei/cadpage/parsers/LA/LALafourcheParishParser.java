package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class LALafourcheParishParser extends FieldProgramParser {

  public LALafourcheParishParser() {
    super(CITY_LIST, "LAFOURCHE PARISH", "LA",
          "CALL:CALL! ID:ID! PLACE:PLACE! ADDR:ADDR! CITY:CITY! DETAILS:EMPTY? INFO:INFO! END");
  }

  @Override
  public String getFilter() {
    return "ledsportal@lpso.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  public final static Pattern SUBJECT_PTN = Pattern.compile("(\\d\\d-\\d+(?:-[A-Z]{3})?) - *(.*)");
  private static final Pattern LA_ZIP_PTN = Pattern.compile(", *LA(?: (\\d{5}))?\\b");
  public final static Pattern DATE_TIME_PTN = Pattern.compile("[; ]*\\b(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d)\\b[- ]*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // New keyword format is a lot easier
    if (body.startsWith("CALL:")) {
      return super.parseMsg(body, data);
    }

    else {
      setFieldList("ID CALL ADDR APT CITY DATE TIME INFO");

      // Extract the message ID from the Subject
      Matcher subMatch = SUBJECT_PTN.matcher(subject);
      if (!subMatch.matches())  return false;
      data.strCallId = subMatch.group(1);
      data.strCall = subMatch.group(2).trim();
      body = stripFieldStart(body, "Intersection of ");
      body = stripFieldEnd(body, " None");

      String[] parts = DATE_TIME_PTN.split(body);
      if (parts.length == 0) return true;
      body = parts[0];


      Matcher match = LA_ZIP_PTN.matcher(body);
      if (match.find()) {
        String zip = match.group(1);
        String addr = body.substring(0,match.start()).trim();
        String info =  body.substring(match.end()).trim();

        int pt = addr.lastIndexOf(',');
        if (pt >= 0) {
          data.strCity = addr.substring(pt+1).trim();
          addr = addr.substring(0, pt).trim();
        }
        if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
        parseAddress(addr, data);
        parseInfo(info, data);
      }

      else {
        String info;
        int pt = body.indexOf(',');
        if (pt >= 0) {
          parseAddress(body.substring(0,pt).trim(), data);
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, body.substring(pt+1).trim(), data);
          info = getLeft();
        } else {
          parseAddress(StartType.START_ADDR, body, data);
          info = getLeft();
        }
        parseInfo(info, data);
      }

      for (int ndx = 1; ndx < parts.length; ndx++) {
        parseInfo(parts[ndx], data);
      }
      return true;
    }
  }

  private void parseInfo(String info, Data data) {
    info = info.trim();
    info = stripFieldEnd(info, ";");
    if (data.strCall.length() == 0) {
      data.strCall = info;
    } else {
      data.strSupp = append(data.strSupp, "\n", info);
    }
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    // Replace "BY-PASS" with "BYPASS" (special case)
    sAddress = sAddress.replace("BY-PASS", "BYPASS");
    return sAddress;
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "THIBODAUX",

      // Towns
      "GOLDEN MEADOW",
      "LOCKPORT",

      // Census-designated places
      "BAYOU BLUE",
      "BAYOU COUNTRY CLUB",
      "CHACKBAY",
      "CHOCTAW",
      "CUT OFF",
      "DES ALLEMANDS",
      "GALLIANO",
      "KRAEMER",
      "LAFOURCHE CROSSING",
      "LAROSE",
      "LOCKPORT HEIGHTS",
      "MATHEWS",
      "PORT FOURCHON",
      "RACELAND",

      // Other areas
      "GHEENS",
      "LEEVILLE",

      // Terrebonne parish
      "SCHRIEVER"
  };
}
