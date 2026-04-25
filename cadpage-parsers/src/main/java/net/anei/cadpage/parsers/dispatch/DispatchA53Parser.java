package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class DispatchA53Parser extends SmartAddressParser {

  public DispatchA53Parser(String city, String state) {
    this(null, city, state);
  }

  public DispatchA53Parser(String[] cityList, String city, String state) {
    super(cityList, city, state);

    setFieldList("ID CALL PLACE ADDR APT X CITY SRC TIME INFO");
  }

  private static final Pattern ID_CALL = Pattern.compile("(?:.*?CFS#? *(\\d+) *- *)?(.*)");
  private static final Pattern INFO_HDR_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private static final Pattern SRC_DISP = Pattern.compile("- ([A-Z]{3,4}) [A-Z0-9]+");
  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("([ A-Z]+?)(?: (TX))?(?: (\\d{5}(?:-?\\d{4})?))?");
  private static final Pattern ST_ZIP_PTN = Pattern.compile("TX(?: \\d{5}(?:-?\\d{4})?)?|\\d{5}(?:-?\\d{4})?");
  private static final Pattern CITY_PTN = Pattern.compile("[ A-Z]+");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //ID from subject... could get call too but there's a case where its incomplete (TXHelotes T2)
    Matcher mat = ID_CALL.matcher(subject);
    if (!mat.matches()) return false;
    data.strCallId = getOptGroup(mat.group(1));

    //get \n\n index to split page by
    String info;
    int nni = body.indexOf("\n\n");
    if (nni >= 0) {
      info = body.substring(nni+2).trim();
      body = body.substring(0,nni).trim();
    } else if ((mat = INFO_HDR_PTN.matcher(body)).find()) {
      info = body.substring(mat.end());
      body = body.substring(0,mat.start());
    } else {
      body = stripFieldEnd(body, " None");
      info = "";
    }

    data.strSupp = INFO_HDR_PTN.matcher(info).replaceAll("\n").trim();

    //split first line of body by commas, return false if more fields than we know what to do with
    String[] fields = body.split(",");
    if (fields.length < 3) return false; //fail if abnormal # of fields

    //CALL ADDR
    data.strCall = fields[0].trim();

    // Now we start from the back
    //SRC required
    int iEnd = fields.length-1;
    mat = SRC_DISP.matcher(fields[iEnd].trim());
    if (mat.matches()) {
      data.strSource = mat.group(1).trim();
      if (--iEnd < 1) return false;
    }

    // Optional city, st zip
    // This match never fails, so we need to check that it found
    // either the state or zip
    Matcher match = CITY_ST_ZIP_PTN.matcher(fields[iEnd].trim());
    if (!match.matches()) return false;
    String city = match.group(1).trim();
    if (!city.equals("TX") &&
        (match.group(1) != null || match.group(2) != null || isCity(city))) {
      data.strCity = match.group(1).trim();
      if (--iEnd < 1) return false;
    }

    // No go, Check for comma between city and state :(
    else {
      match = ST_ZIP_PTN.matcher(fields[iEnd].trim());
      if (match.matches()) {
        city = fields[--iEnd].trim();
        if (!CITY_PTN.matcher(city).matches()) return false;
        data.strCity = city;
        if (--iEnd < 1) return false;
      }
    }

    // Make sure we found something
    if (data.strSource.isEmpty() && data.strCity.isEmpty()) return false;

    // OK, whats left can be anywhere from 1 to 3 fields
    // An optional place
    // A required address
    // An optional cross street

    String addr;
    switch (iEnd) {
    case 1:
      addr = fields[1].trim();
      break;

    case 2:
      String field1 = fields[1].trim();
      String field2 = fields[2].trim();
      if (field2.equals("NONE")) {
        addr = field1;
      } else if (checkAddress(field1) >= checkAddress(field2)) {
        addr = field1;
        data.strCross = field2;
        if (data.strCross.equals("NONE")) data.strCross = "";
      } else {
        data.strPlace = field1;
        addr = field2;
      }

      break;

    case 3:
      data.strPlace = fields[1].trim();
      addr = fields[2].trim();
      data.strCross = fields[3].trim();
      if (data.strCross.equals("NONE")) data.strCross = "";
      break;

    default:
      return false;
    }

    parseAddress(addr.replace('@', '&'), data);
    return true;
  }

}