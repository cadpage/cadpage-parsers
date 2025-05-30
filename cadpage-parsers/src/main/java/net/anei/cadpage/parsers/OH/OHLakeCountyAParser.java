package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;



public class OHLakeCountyAParser extends FieldProgramParser {

  public OHLakeCountyAParser() {
    super(CITY_CODES, "LAKE COUNTY", "OH",
           "Location:ADDR/S? APT:APT? Cross_Streets:X! ( Type_Code:CALL! | TYPE_CODE:CALL! ) Sub_Type:CALL! ( Time:TIME! | TIME:TIME! ) Comments:INFO CommonPlace:PLACE? CFS#:ID?");
  }

  @Override
  public String getFilter() {
    return "ingr.com@archwireless.net,ingr.com@usamobility.net,Central@lakecountyohio.gov";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override public boolean splitKeepLeadBreak() { return true; }
      @Override public boolean splitKeepTrailBreak() { return false; }
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public int splitBreakLength() { return 151; }
      @Override public int splitBreakPad() { return 3; }
    };
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.length() > 150 && body.charAt(150)==' ') {
      body = body.substring(0,150) + body.substring(151);
    }
    body = body.replace("CFS#:", " CFS#:");
    if (!super.parseMsg(body, data)) return false;
    if (data.strAddress.startsWith("&")) {
      data.strPlace = append(data.strAddress.substring(1).trim(), " - ", data.strPlace);
      data.strAddress = "";
    }
    if (data.strAddress.length() == 0) {
      parseAddress(data.strCross, data);
      data.strCross = "";
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "PLACE " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CO", "CONCORD TWP",
      "FH", "FAIRPORT HARBOR",
      "GR", "GRAND RIVER",
      "LE", "LEROY TWP",
      "MA", "MADISON",
      "MV", "MADISON",
      "NP", "NORTH PERRY",
      "PA", "PAINSVILLE",
      "PE", "PERRY",
      "PC", "PAINSVILLE",
      "PV", "PERRY",
      "WA", "WAITE HILL",
      "WH", "WILLOUGHBY HILLS"

  });
}
