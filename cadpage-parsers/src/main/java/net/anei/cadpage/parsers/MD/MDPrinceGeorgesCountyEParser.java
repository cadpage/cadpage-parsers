package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Prince Georges County, MD (variant E)
 */
public class MDPrinceGeorgesCountyEParser extends MDPrinceGeorgesCountyBaseParser {

  private static final Pattern PREFIX_PTN = Pattern.compile("(?:.*? - |:)?DISPATCH From [A-Z0-9]*: *");
  private static final Pattern BAD_ID_PTN = Pattern.compile("PF\\d{6,}:");
  private static final Pattern ID_PTN = Pattern.compile("^(?:TR +|.* / )?([FL]\\d{6,}):");
  private static final Pattern TRAILER = Pattern.compile(" - From [A-Z0-9]+ (\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)$");
  private static final Pattern AT_PTN = Pattern.compile("\\bAT\\b", Pattern.CASE_INSENSITIVE);

  public MDPrinceGeorgesCountyEParser() {
    super("CODE? CALL ADDR! PP? AT? X? PP2? ( CITY ST CH | CITY CH | CH% ) BOX MAP INFO+ Units:UNIT% UNIT+");
  }

  @Override
  public String getFilter() {
    return "@alert.co.pg.md.us,@c-msg.net,@everbridge.net,14100,12101,87844,88911,89361";
  }

  @Override
  public boolean parseMsg(String body, Data data) {

    Matcher match = PREFIX_PTN.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());

    // rule out version G format
    if (BAD_ID_PTN.matcher(body).find()) return false;

    match = ID_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strCallId = match.group(1);
      body = body.substring(match.end()).trim();
    }

    match = TRAILER.matcher(body);
    if (match.find()) {
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      body = body.substring(0,match.start()).trim();
    }

    body = body.replace(" Unit:", " Units:");
    if (!parseFields(body.split(","), data)) return false;
    if (data.strUnit.length() == 0) data.strUnit = data.strSource;
    data.strAddress = AT_PTN.matcher(data.strAddress).replaceAll("&");

    // Truncated messages may confuse PP field for address
    if (data.strAddress.length() < 5 || data.strAddress.contains("<")) return false;

    if (data.strState.equals("MD")) data.strState = "";

    // If they did not specify a city, see if we can deduce it from a mutual aid code
    MDPrinceGeorgesCountyGParser.fixCity(data);

    return data.strCallId.length() > 0 || data.strChannel.length() > 0;
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram().replace("BOX", "BOX CITY ST") + " DATE TIME";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new CodeField("[A-Z]+\\d?");
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PP")) return new SkipField("[A-Z]{1,2}|", true);
    if (name.equals("AT")) return new AtField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("PP2")) return new SkipField("(?!H$)[A-Z]{1,2} *(?:<\\d.*)?|<\\d.*|", true);
    if (name.equals("CH")) return new ChannelField("(?:T?G?|FX)[A-F]\\d{1,2}|H", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  // The AT field starts with an at keyword
  // It indicates that this is the real address, and what we originally
  // parsed as an address is a place name
  private class AtField extends AddressField {

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("at ")) return false;
      data.strPlace = data.strAddress;
      data.strAddress = "";
      parse(field.substring(3).trim(), data);
      if (data.strAddress.equals(data.strPlace)) data.strPlace = "";
      return true;
    }
  }

  // Cross field only exist if it has the correct keyword
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("btwn ")) return false;
      field = field.substring(5).trim();
      super.parse(field, data);
      return true;
    }
  }

  // Info field drops ProQA comments
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.contains("ProQA recommends dispatch")) return;
      super.parse(field, data);
    }
  }

  // Unit fields join together with comma separators
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("WI")) {
        if (!data.strCall.contains("(Working)")) data.strCall += " (Working)";
      } else {
        data.strUnit = append(data.strUnit, ",", field);
      }
    }
  }
}
