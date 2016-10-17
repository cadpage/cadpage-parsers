package net.anei.cadpage.parsers.NY;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.DateFormat;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA40Parser;

public class NYAlbanyCountyCParser extends DispatchA40Parser {

  public NYAlbanyCountyCParser() {
    super(CITY_LIST, "ALBANY COUNTY", "NY");
    setFieldList("SRC CALL PLACE ADDR APT CITY INFO DATE TIME");
    setupMultiWordStreets("NORTH GREENBUSH");
    addInvalidWords("HRS");
  }

  private static Pattern BODY_TIME = Pattern.compile("(.*?)-[A-Z]{3}\\((\\d{1,2}/\\d{1,2}/\\d{4}) (\\d{1,2}:\\d{2}:\\d{2} [AP]M)\\)");
  private static DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private static Pattern TRIM_LEADING_DASH_JUNK = Pattern.compile("[- ]*(.*?)"); // "-- -body"
  private static Pattern EXTRA_DELIM = Pattern.compile(" - |, ");
  private static Pattern SOURCE_PTN = Pattern.compile("[A-Z]{1,3}FD");
  private static Pattern SOURCE_CALL_PTN = Pattern.compile("([A-Z]{1,3}FD) +(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    // parse trailing date time construct
    Matcher mat = BODY_TIME.matcher(body);
    if (!mat.matches()) return false;
    body = mat.group(1).trim();
    data.strDate = mat.group(2);
    setTime(TIME_FMT, mat.group(3), data);

    // use base class to try and parse Nature Location Comments format
    if (super.parseMsg(body, data)) return true;

    // if that fails, we can handle a number of different formats
    // First strip off any leading dashes and spaces
    Matcher tMat = TRIM_LEADING_DASH_JUNK.matcher(body);
    if (tMat.matches()) body = tMat.group(1);

    // If we can break things up with comma and dash delimiters
    // see if it fits a SRC - CALL - PLACE ADDR - CITY. INFO pattern
    String saveBody = body;
    String info = "";
    int pt = body.lastIndexOf(". ");
    if (pt >= 0) {
      info = body.substring(pt+2).trim();
      body = body.substring(0,pt);
    }
    String[] parts = EXTRA_DELIM.split(body);
    for (int jj = 0; jj < parts.length; jj++) {
      parts[jj] = parts[jj].trim();
    }
    int apt = parts.length-1;
    if (apt > 0 && isCity(parts[apt])) apt--;
    StartType st = StartType.START_PLACE;
    int flags = FLAG_CHECK_STATUS | FLAG_NO_CITY | FLAG_ANCHOR_END;
    if (apt == 0) {
      st = StartType.START_CALL;
      flags |= FLAG_START_FLD_REQ;
    }
    Result res = parseAddress(st, flags, parts[apt]);
    if (res.getStatus(0) > STATUS_STREET_NAME) {
      res.getData(data);
      if (apt < parts.length-1) data.strCity = parts[apt+1];

      int spt = 0;
      if (apt-spt > 1 && SOURCE_PTN.matcher(parts[spt]).matches()) {
        data.strSource = parts[spt++];
      }
      if (apt-spt > 1) data.strPlace = parts[--apt];

      for ( ; spt<apt; spt++) {
        data.strCall = append(data.strCall, ", ", parts[spt]);
      }
      
      // See if we can find a leading source field
      if (data.strSource.length() == 0) {
        mat = SOURCE_CALL_PTN.matcher(data.strCall);
        if (mat.matches()) {
          data.strSource = mat.group(1);
          data.strCall = mat.group(2);
        }
      }
      
      // If we did not find a place field, see if we can pull one from the call field
      if (data.strPlace.length() == 0) {
        pt = data.strCall.indexOf(" AT ");
        if (pt >= 0) {
          data.strPlace = data.strCall.substring(pt+4).trim();
          data.strCall = data.strCall.substring(0,pt).trim();
        } else {
          String call = CALL_LIST.getCode(data.strCall, true);
          if (call != null) {
            data.strPlace = data.strCall.substring(call.length()).trim();
            data.strCall = call;
          }
        }
      }
      data.strSupp = info;
      return true;
    }

    // No, go, report as general alert
    data.strCall = "GENERAL ALERT";
    data.strAddress = "";
    data.strApt = "";
    data.strPlace = saveBody;

    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "CARBON MONOXIDE DETECTOR ACTIVATION",
      "CO ALARM ACTIVATION",
      "CO DETECTOR ACTIVATION NO ILLNESS",
      "FIRE ALARM ACTIVATION",
      "FIRE ALARM DROP",
      "REPORTED STRUCTURE FIRE",
      "SMOKE CONDITION STRUCTURE FIRE",
      "STRUCTURE FIRE",
      "VEHICLE FIRE"
  );

  private static final String[] CITY_LIST = new String[] { 
    // cities
    "ALBANY",
    "COHOES",
    "WATERVLIET",
    
    // towns
    "BERNE",
    "BETHLEHEM",
    "COEYMANS",
    "COLONIE",
    "EAST GREENBUSH",
    "GREEN ISLAND",
    "GREENBUSH",
    "GUILDERLAND",
    "KNOX",
    "NEW SCOTLAND",
    "NORTH GREENBUSH",
    "RENSSELAERVILLE",
    "WESTERLO",
    
    // villages
    "ALTAMONT",
    "MENANDS",
    "RAVENA",
    "SCHODACK",
    "VOORHEESVILLE"
  };
}
