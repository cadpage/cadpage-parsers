package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAAugustaParser extends FieldProgramParser {

  public GAAugustaParser() {
    super("AUGUSTA", "GA",
          "CALL? ADDRCITY/S6 X ( SELECT/1 INFO/N+? UNIT_CH/Z EMPTY CALL! EMPTY+? " +
                              "| INFO UNIT! EMPTY? CH " +
                              "| INFO/N+? CH_INFO! INFO/N+ ) END");
  }

  @Override
  public String getFilter() {
    return "dispatch@augustaga.gov";
  }

  private static final Pattern MASH_PTN = Pattern.compile("(.*?[a-z][<\\)]?)([A-Z0-9].*?[a-z0-9])([A-Z].*|)\n");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    int pt = body.indexOf("\n\n\n___");
    if (pt >= 0) body = body.substring(0,pt);

    Matcher match = MASH_PTN.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("1");
      body = match.group(1)+"\n"+match.group(2)+"\n"+match.group(3)+"\n"+body.substring(match.end()).replace("\n..", "\n").replace("..", "\n");
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
    if (name.equals("CH")) return new ChannelField("((?:FIRE TAC|EMS) \\d+|\\d+)", true);
    if(name.equals("UNIT_CH")) return new MyUnitChannelField();
    if (name.equals("CH_INFO")) return new MyChannelInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {

      // Override any previous value
      data.strCall = field;
    }
  }

  private static final Pattern ADDR_CITY_PTN = Pattern.compile("[-@&A-Z0-9 ]+, *[A-Z][a-z]+(?: +[A-Z][a-z]+)?");

  private class MyAddressCityField extends AddressCityField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!ADDR_CITY_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private static final Pattern TRAIL_CH_PTN = Pattern.compile("(?:FIRE TAC|EMS) \\d+$");
  private static final Pattern UNIT_PTN = Pattern.compile("((?:\\b[A-Z]{0,4}\\d{0,3}\\b,? *)+)");
  private static final Pattern BAD_UNIT_PTN = Pattern.compile("[^, ] ");
  private static final Pattern INFO_UNIT_PTN = Pattern.compile("(.*)(HF(?:,.*)?)");

  private class MyUnitChannelField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.isEmpty()) return false;
      boolean good = false;
      Matcher match = TRAIL_CH_PTN.matcher(field);
      if (match.find()) {
        good = true;
        data.strChannel = match.group();
        field = field.substring(0,match.start()).trim();
        if (field.isEmpty()) return true;
      }
      if (UNIT_PTN.matcher(field).matches() && !BAD_UNIT_PTN.matcher(field).find()) {
        data.strUnit = field;
        return true;
      } else {
        match = INFO_UNIT_PTN.matcher(field);
        if (match.matches()) {
          good = true;
          field = match.group(1).trim();
          data.strUnit = match.group(2);
        }
      }
      if (good) {
        data.strSupp = append(data.strSupp, "\n", field);
        return true;
      } else {
        return false;
      }
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

  private static final Pattern CHANNEL_INFO_PTN = Pattern.compile("((?:FIRE TAC|EMS) \\d+) */ *(.*)", Pattern.DOTALL);
  private static final Pattern INFO_CHANNEL_PTN = Pattern.compile("(.*?) */ *((?:FIRE TAC|EMS) \\d+)", Pattern.DOTALL);

  private class MyChannelInfoField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CHANNEL_INFO_PTN.matcher(field);
      if (match.matches()) {
        data.strChannel = match.group(1);
        data.strSupp = append(data.strSupp, "\n", match.group(2));
        return true;
      } else if ((match = INFO_CHANNEL_PTN.matcher(field)).matches()) {
        data.strSupp = append(data.strSupp, "\n", match.group(1));
        data.strChannel = match.group(2);
        return true;
      } else {
        return false;
      }
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CH INFO";
    }
  }
}
