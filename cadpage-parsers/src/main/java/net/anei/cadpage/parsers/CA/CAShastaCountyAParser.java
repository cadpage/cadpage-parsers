package net.anei.cadpage.parsers.CA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


/**
 * Shasta County, CA
 */
public class CAShastaCountyAParser extends FieldProgramParser {

  public CAShastaCountyAParser() {
    super("SHASTA COUNTY", "CA",
          "( SELECT/RR ID1 CALL ADDR0 INFO! INFO/N+ " +
          "| SELECT/V3 CALL ADDR0! X:INFO! GPS/Y ID3 EMPTY UNIT! INFO/N+? GPS1 " +
          "| SELECT/V4 CALL ADDR0 APT! Xstreet:X! UNIT ID1 UNIT UNIT/S INFO/N+? GPS1 " +
          "| ID2 CALL ADDR1 ADDR2 ADDR3! Remarks:INFO INFO/N+? GPS2 Resources:UNIT " +
          "| CALL ADDR1 ADDR2 ADDR3! Map:MAP! ID1! UNIT INFO/N+? GPS1 )");
  }

  @Override
  public String getFilter() {
    return "vtext.com@gmail.com,5304482408,5304109246,cad@fire.ca.gov,admin@streetwisecadfeed.com,jchipper@gmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    body = body.replace("</br>", "<br>");
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    int pt = body.indexOf("\n-- \n");
    if (pt >= 0) body = body.substring(0,pt).trim();

    setSelectValue("");
    if (body.startsWith("CLOSE:")) {
      setSelectValue("RR");
      body = body.substring(6).trim();
      data.msgType = MsgType.RUN_REPORT;
    }

    else if ((pt = body.indexOf("  X:")) >= 0) {
      setSelectValue("V3");
      body = body.substring(0,pt) + ';' + body.substring(pt);
    }

    else if ((pt = body.indexOf(" Xstreet ")) >= 0) {
      setSelectValue("V4");
      pt += 8;
      body = body.substring(0,pt) + ':' + body.substring(pt);
    }

    String[] flds = body.split(" *; *");
    if (flds.length < 4) flds = body.split("\n");
    return parseFields(flds,  4, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID1")) return new IdField("Inc# *(\\d*)", true);
    if (name.equals("ID2")) return new IdField("Incident #(\\d*)", true);
    if (name.equals("ID3")) return new IdField("([A-Z]{5} \\d+)\\]", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR0")) return new MyAddressField(0);
    if (name.equals("ADDR1")) return new MyAddressField(1);
    if (name.equals("ADDR2")) return new MyAddressField(2);
    if (name.equals("ADDR3")) return new MyAddressField(3);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+): *(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private List<AddrStat> addressLines = new ArrayList<AddrStat>();

  private class MyAddressField extends AddressField {

    private int type;

    public MyAddressField(int type) {
      this.type = type;
    }

    @Override
    public void parse(String field, Data data) {

      // First address line resets the address list
      // and does some extra parsing
      if (type <= 1) {
        addressLines.clear();

        int pt = field.indexOf('@');
        if (pt >= 0) {
          data.strPlace = field.substring(0,pt).trim();
          field = field.substring(pt+1).trim();
        }

        int limit = 0;
        if (field.startsWith("=L(")) {
          limit = field.indexOf(')')+1;
          if (limit <= 0) limit = field.length();
        }
        if (limit < field.length() && field.endsWith(")")) {
          pt = field.lastIndexOf('(');
          if (pt < limit) abort();
          data.strPlace = append(data.strPlace, " - ", field.substring(pt+1, field.length()-1).trim());
          field = field.substring(0,pt).trim();
        }
        pt = field.lastIndexOf(',');
        if (pt >= limit) {
          String src = field.substring(pt+1).trim();
          field = field.substring(0,pt).trim();
          if (src.startsWith("STA")) {
            data.strSource = src;
          } else {
            data.strCity = convertCodes(src, CITY_CODES);
          }
        }
      }

      // All address lines go into the address line table
      if (field.length() > 0) addressLines.add(new AddrStat(field));

      // And the last address line gets to work out just what goes where
      // Life gets complicated, we have up to three address fields, but the best address
      // is not necessarily the first.  Sort them by address quality, the first
      // one will be the address, others will be cross streets
      if (type == 0 || type == 3) {
        Collections.sort(addressLines);
        if (addressLines.size() == 0) return;
        parseAddress(addressLines.get(0).address, data);
        for (int ndx = 1; ndx < addressLines.size(); ndx++) {
          data.strCross = append(data.strCross, " & ", addressLines.get(ndx).address);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT SRC CITY PLACE X";
    }
  }

  private class AddrStat implements Comparable<AddrStat>{
    String address;
    int status;

    AddrStat(String address) {
      this.address = address;
      this.status = -1;
      if (address.length() == 0) return;

      this.status = checkAddress(address);
      if (!address.contains("BLK")) this.status += 100;
      if (!address.contains("/")) this.status += 10;
    }

    @Override
    public int compareTo(AddrStat adst) {
      return -(this.status - adst.status);
    }
  }

  private static final Pattern[] GPS_PTNS = new Pattern[]{
    Pattern.compile("<a .*href=[\"']http://maps.google.com/\\?q=([-+]?\\d+\\.\\d{4,},[-+]?\\d+\\.\\d{4,})[\"']"),
    Pattern.compile("http://maps.google.com/\\?q=([-+]?\\d+\\.\\d{4,},[-+]?\\d+\\.\\d{4,})")
  };
  private static final String[] GPS_STRS = new String[]{
    "<a href=\"http://maps.google.com/?q=",
    "http://maps.google.com/?q="
  };
  private class MyGPSField extends GPSField {

    private Pattern gpsPtn;
    private String gpsStr;

    public MyGPSField(int type) {
      gpsPtn = GPS_PTNS[type-1];
      gpsStr = GPS_STRS[type-1];
    }

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      Matcher match = gpsPtn.matcher(field);
      if (match.find()) {
        setGPSLoc(match.group(1), data);
        field = field.substring(0, match.start()).trim();
        int pt = field.indexOf("<a ");
        if (pt >= 0) field = field.substring(0,pt).trim();
        data.strSupp = append(data.strSupp, "\n", field);
        return true;
      }

      // Check for truncated GPS field
      if (gpsStr.startsWith(field) || field.startsWith(gpsStr)) return true;
      return false;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "INFO GPS";
    }
  }

  @Override
  public String adjustMapCity(String city) {
    city = convertCodes(city, CITY_MAP);
    return super.adjustMapCity(city);
  }

  private static final Properties CITY_MAP = buildCodeTable(new String[]{
      "BOWMAN",           "COTTONWOOD",
      "CASSEL",           "FALL RIVER MILLS",
      "CENTERVILLE",      "REDDING",
      "DAY",              "MCARTHUR",
      "JELLYS FERRY",     "RED BLUFF",
      "JONES VALLEY",     "REDDING",
      "KESWICK",          "REDDING",
      "MOUNTAIN GATE",    "REDDING",
      "SHASTA COLLEGE",   "REDDING",
      "SHASTA LAKE",      "REDDING",
      "SOLIDER MTN",      "FALL RIVER MILLS",
      "WEST VALLEY",      "ANDERSON",
      "WIDOW MOUNTAIN",   "MCARTHUR",
      "WNPS",             "SHASTA"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BELLAVISTA",       "BELLA VISTA",
      "BIGBEND",          "BIG BEND",
      "DAY_LMU",          "DAY",
      "FALLRIVER",        "FALL RIVER MILLS",
      "JELLYSFERRY",      "JELLYS FERRY",
      "JONESVALLEY",      "JONES VALLEY",
      "HATCRK",           "HAT CREEK",
      "MCARTHUR_FPD_SRA", "MCARTHUR",
      "MCARTHUR_FPD_LRA", "MCARTHUR",
      "MONTGOMERYCK",     "MONTGOMERY CREEK",
      "MTNGATE",          "MOUNTAIN GATE",
      "OLDSTA",           "OLD STATION",
      "PALOCEDRO",        "PALO CEDRO",
      "REDDINGCTY",       "REDDING",
      "SHASTACOLL",       "SHASTA COLLEGE",
      "SHASTALKCTY",      "SHASTA LAKE",
      "SOLIDERMTN",       "SOLIDER MTN",
      "WESTVALLEY",       "WEST VALLEY",
      "WIDOW_MOUNTAIN",   "WIDOW MOUNTAIN"
  });
}
