package net.anei.cadpage.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class AddressParser {

  private String delims;

  private String place, apt, addrExt, state;

  private static final Pattern ADDR_GPS_PTN = Pattern.compile("[-+]?(?:\\d+ +\\d+ +)?\\d+\\.\\d+\\b.*|Y:.*");
  private static final Pattern ADDR_APT_PTN1 = Pattern.compile("(.*)\\b(?:APARTMENT|APT|LOT|RM|ROOM|SUITE|UNIT)[:# ]+([A-Z]?\\d+[A-Z]?|[A-Z])", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_APT_PTN2 = Pattern.compile("(?:APARTMENT(?!S)|APT|LOT|RM|ROOM|SUITE|UNIT)?[# ]*([A-Z]?\\d+[A-Z]?|[A-Z])", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_EXT_PTN = Pattern.compile("[NSEW]B|MM *\\d+.*");

  public AddressParser() {
    this(":;,");
  }

  public AddressParser(String delims) {
    this.delims = delims;
  }

  public String parse(String field) {

    place = apt = addrExt = "";
    state = null;
    int ept = field.length();
    for ( int pt = field.length()-1; pt >= 0; pt--) {
      char chr = field.charAt(pt);
      if (delims.indexOf(chr) >= 0) {
        String fld = field.substring(pt+1, ept).trim();
        if (chr == ':') fld = MsgParser.stripFieldStart(fld, "@");
        if (!process(chr, fld)) break;
        ept = pt;
      }
    }
    return postProcess(field.substring(0, ept).trim());
  }

  protected boolean process(char chr, String fld) {

    if (ADDR_GPS_PTN.matcher(fld).matches()) return false;

    Matcher match;
    if (chr == ':') {
      setPlace(fld);
    }
    else if ((match = ADDR_APT_PTN1.matcher(fld)).matches()) {
      setPlace(match.group(1).trim());
      setApt(match.group(2).trim());
    } else if ((match = ADDR_APT_PTN2.matcher(fld)).matches()) {
      setApt(match.group(1).trim());
    } else if (StateCodes.isStateCode(fld)) {
      state = fld;
    } else if (ADDR_EXT_PTN.matcher(fld).matches()) {
      addrExt = append(fld, " ", addrExt);
    } else {
      setPlace(fld);
    }
    return true;
  }

  protected String postProcess(String field) {
    return field;
  }

  protected void setPlace(String fld) {
    place = append(fld, " - ", place);
  }

  protected void setApt(String fld) {
    apt = append(fld, "-", apt);
  }

  public void merge(Data data) {
    data.strAddress = MsgParser.append(data.strAddress, " ", addrExt);
    data.strPlace = append(data.strPlace, " - ", place);
    data.strApt = append(data.strApt, "-", apt);
    if (state != null) data.strState = state;
  }

  protected String append(String fld1, String connect, String fld2) {
    if (fld1.contains(fld2)) return fld1;
    if (fld2.contains(fld1)) return fld2;
    return fld1+connect+fld2;
  }

  public String getFieldNames() {
    StringBuilder sb = new StringBuilder();
    getFieldNames(sb);
    return sb.toString();
  }

  public void getFieldNames(StringBuilder sb) {
    if (!place.isEmpty()) sb.append(" PLACE");
    if (!apt.isEmpty()) sb.append(" APT");
    if (state != null) sb.append(" ST");
  }
}
