package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAAugustaParser extends FieldProgramParser {

  public GAAugustaParser() {
    super("AUGUSTA", "GA",
          "CALL? ADDRCITY/S6 X ( SELECT/1 INFO/N+? UNIT_CH EMPTY CALL! EMPTY+? | INFO UNIT CH! ) END");
  }

  @Override
  public String getFilter() {
    return "dispatch@augustaga.gov";
  }

  private static final Pattern MASH_PTN = Pattern.compile("(.*?[a-z])([A-Z0-9].*?[a-z0-9])([A-Z].*|)\n");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    int pt = body.indexOf("\n\n\n___");
    if (pt < 0) return false;
    body = body.substring(0,pt);

    Matcher match = MASH_PTN.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("1");
      body = match.group(1)+"\n"+match.group(2)+"\n"+match.group(3)+"\n"+body.substring(match.end()).replace("..", "\n");
      return parseFields(body.split("\n", -1), data);
    } else {
      setSelectValue("2");
      return parseFields(body.split("\n\n", -1), data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("UNIT")) return new UnitField("(?:\\b[A-Z]{0,4}\\d{0,3}\\b,? *)*", true);
    if (name.equals("CH")) return new ChannelField("(?:FIRE TAC|EMS) \\d+|", true);
    if(name.equals("UNIT_CH")) return new MyUnitChannelField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {

      // Override any previous value
      data.strCall = field;
    }
  }
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private static final Pattern UNIT_CH_PTN =
      Pattern.compile("(.*?)((?:[A-Z]{0,4}\\d{0,3},? *)*)((?:FIRE TAC|EMS) \\d+)");

  private class MyUnitChannelField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = UNIT_CH_PTN.matcher(field);
      if (!match.matches()) return false;
      String info = match.group(1).trim();
      data.strSupp = append(data.strSupp, "/n", stripFieldEnd(info, ".."));
      data.strUnit = match.group(2).trim();
      data.strChannel = match.group(3);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "INFO UNIT CH";
    }

  }
}
