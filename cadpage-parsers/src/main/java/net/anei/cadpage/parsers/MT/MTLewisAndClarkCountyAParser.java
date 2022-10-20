package net.anei.cadpage.parsers.MT;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MTLewisAndClarkCountyAParser extends FieldProgramParser {

  public MTLewisAndClarkCountyAParser() {
    super(CITY_CODES, "LEWIS AND CLARK COUNTY", "MT",
           "UNITS:UNIT! CFS:ID! TYP:CALL! LOC:ADDRCITY! BUSN:PLACE APT:APT CMP:NAME PHONE:PHONE CMNTS:INFO+ MEDS:SKIP MAP:MAP MAP+? TERM INFO+");
  }

  private static final Pattern MARKER = Pattern.compile("HELENA 911[: ] *(?:\\(.*?\\) *)?");

  @Override
  protected boolean parseMsg(String body, Data data) {

    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());

    int pt = body.indexOf(" TXT STOP");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseFields(body.split("\n"), data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("TERM")) return new SkipField("\\*{5,}", true);
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("#", "");
      super.parse(field, data);
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("U:")) field = field.substring(2).trim();
      super.parse(field, data);
    }
  }

  private class MyMapField extends MapField {

    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "MAP:");
      field = stripFieldStart(field, "MAP ");
      data.strMap = append(data.strMap, " - ", field);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "[]",     "",
      "[AUG]",  "AUGUSTA",
      "[CAS]",  "CASCADE",
      "[CCR]",  "CANYON CREEK",
      "[CRG]",  "CRAIG",
      "[EHEL]", "EAST HELENA",
      "[HLN]",  "HELENA",
      "[LN]",   "LINCOLN",
      "[MARY]", "MARYSVILLE",
      "[WCR]",  "WOLF CREEK"
  });
}
