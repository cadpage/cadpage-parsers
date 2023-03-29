package net.anei.cadpage.parsers.ZCAON;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZCAONChathamKentParser extends FieldProgramParser {

  public ZCAONChathamKentParser() {
    super(CITY_CODES, "CHATHAM-KENT", "ON",
          "CALL:CALL! ( PLACE:PLACE! ADDR:ADDR! XSTR:X CITY:CITY XSTR1:X XSTR2:X ID:ID | ) DATE:DATE TIME:TIME! " +
              "( TIME_OUT:SKIP! EVENT_COMMENTS:INFO | ) " +
              "( LATITUDE:GPS1! LONGITUDE:GPS2! ID:ID ( UNITS:UNIT SOURCE:SKIP? | Disp:UNIT | ) | ) INFO:INFO");
    addRoadSuffixTerms("LI");
  }

  @Override
  public String getFilter() {
    return "@chatham-kent.ca";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern EOM_MARK_PTN = Pattern.compile(" Access Token Refreshed | CALLBACK ROSTER ASSIGNED:| DISPATCH TIME:| Dispath Time:|\n-{5,}\n");
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = EOM_MARK_PTN.matcher(body);
    if (match.find()) body = body.substring(0, match.start()).trim();
    String[] flds = body.split(";");
    if (flds.length >=  4) {
      if (!super.parseFields(flds, data)) return false;
    } else {
      if (!super.parseMsg(body, data)) return false;
    }
    if (data.strAddress.length() == 0) {
      parseAddress(data.strCross, data);
      data.strCross = "";
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      for (String part : field.split(": @")) {
        part = part.trim();
        if (data.strAddress.length() == 0) {
          String apt = "";
          int pt = part.indexOf(',');
          if (pt >= 0) {
            apt = part.substring(pt+1).trim();
            part = part.substring(0,pt).trim();
          }
          parseAddress(part, data);
          data.strApt = append(data.strApt, "-", apt);
        }  else if (!data.strPlace.contains(part)) {
          data.strPlace = append(data.strPlace, " - ", part);
        }
      }
    }
  }

  private static final Pattern LI_PTN = Pattern.compile("\\bLI\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern SR_PTN = Pattern.compile("\\bSR\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    addr = LI_PTN.matcher(addr).replaceAll("LINE");
    addr = SR_PTN.matcher(addr).replaceAll("SIDE RD");
    return super.adjustMapAddress(addr);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BLE", "BLENHEIM",
      "CHA", "CHATHAM TWP",
      "CMD", "CAMDEN TWP",
      "CTH", "CHATHAM",
      "DET", "DET",              // Bothwell??
      "DOV", "DOVER TWP",
      "HAR", "HARWICH TWP",
      "HOW", "HOWARD TWP",
      "MRV", "MORAVIAN INDIAN RESERVE",
      "ORF", "ORFORD TWP",
      "RAL", "RALEIGH TWP",
      "ROM", "ROMNEY TWP",
      "TLB", "TILBURY EAST TWP",
      "WLC", "WALLACEBURG",
      "ZON", "ZONE TWP"
  });
}
