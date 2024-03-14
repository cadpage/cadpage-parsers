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

    setFieldList("ID CALL ADDR APT X CITY SRC TIME INFO");
  }

  private static Pattern ID_CALL = Pattern.compile(".*?CFS#? *(\\d+) *- *(.*)");
  private static Pattern SRC_DISP = Pattern.compile("- ([A-Z]{3,4}) [A-Z0-9]+");
  private static Pattern CITY_ST_ZIP_PTN = Pattern.compile("([ A-Z]+?)(?: (TX))?(?: (\\d{5}(?:-?\\d{4})?))?");
  private static Pattern TIME = Pattern.compile("(\\d{2}:\\d{2}:\\d{2}) *-{3,4} *");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //ID from subject... could get call too but there's a case where its incomplete (TXHelotes T2)
    Matcher mat = ID_CALL.matcher(subject);
    if (!mat.matches()) return false;
    data.strCallId = mat.group(1);

    //get \n\n index to split page by
    String trail = "";
    int nni = body.indexOf("\n\n");
    if (nni >= 0) {
      trail = body.substring(nni+2).trim();
      body = body.substring(0,nni).trim();
    }

    //split first line of body by commas, return false if more fields than we know what to do with
    String[] fields = body.split(",");
    if (fields.length > 5 || fields.length < 3) return false; //fail if abnormal # of fields

    //CALL ADDR
    data.strCall = fields[0].trim();
    parseAddress(fields[1].trim().replace('@', '&'), data);

    // Now we start from the back
    //SRC required
    int iEnd = fields.length-1;
    mat = SRC_DISP.matcher(fields[iEnd].trim());
    if (mat.matches()) {
      data.strSource = mat.group(1).trim();
      iEnd--;
    }

    // Optional city
    Matcher match = CITY_ST_ZIP_PTN.matcher(fields[iEnd].trim());
    if (match.matches()) {

      // This match never fails any more, so we have to make sure that
      // a state or zip code was found
      String city = match.group(1).trim();
      if (match.group(2) != null || match.group(3) != null || isCity(city)) {
        data.strCity = city;
        iEnd--;
      }
    }

    // Make sure we found something
    if (data.strSource.isEmpty() && data.strCity.isEmpty()) return false;

    // If we haven't used anything up, we have room for one cross street
    if (iEnd > 2) return false;
    if (iEnd == 2) {
      String cross = fields[2].trim();
      if (!cross.equals("NONE")) data.strCross = cross;
    }

    //INFO
    mat = TIME.matcher(trail);
    if (mat.find()) data.strTime = mat.group(1);
    data.strSupp = mat.replaceAll("");

    //victory!
    return true;
  }

}