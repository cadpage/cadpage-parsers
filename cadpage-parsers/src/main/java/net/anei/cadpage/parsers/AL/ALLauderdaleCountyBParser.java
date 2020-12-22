package net.anei.cadpage.parsers.AL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Lauderdale County, AL (B)
 */
public class ALLauderdaleCountyBParser extends FieldProgramParser {

  public ALLauderdaleCountyBParser() {
    super(ALLauderdaleCountyAParser.CITY_TABLE, "LAUDERDALE COUNTY", "AL",
        "Pri:SRC? Address:ADDR/S! Time:TIME! Cross_Streets:X! Event_Type:CALL! Re:INFO!");
    setBreakChar('-');
  }

  @Override
  public String getFilter() {
    return "@everbridge.net,88911,87844,89361";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (body.startsWith("<!doctype html>")) {
      int pt1 = body.indexOf("<!-- Messages start-->\n");
      if (pt1 >= 0) {
        pt1 += 23;
        int pt2 = body.indexOf("<!-- Messages end-->\n", pt1);
        if (pt2 < 0) return false;
        body = body.substring(pt1, pt2);
      }
    }
    return super.parseHtmlMsg(subject, body, data);
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("([A-Z]{2,3}\\d{8}) EV- +.*");
  private static final Pattern MASTER = Pattern.compile("([A-Z]{2,3}\\d{8}) EV- .*? - ((?:Pri|Address)-.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      Matcher match = SUBJECT_PTN.matcher(subject);
      if (match.matches()) {
        data.strCallId = match.group(1);
        break;
      }

      match = MASTER.matcher(body);
      if (match.matches()) {
        data.strCallId = match.group(1);
        body = match.group(2);
        break;
      }

      if (isPositiveId()) {
        setFieldList("INFO");
        data.msgType = MsgType.GEN_ALERT;
        for (String line : body.split("\n")) {
          line = line.trim();
          if (line.startsWith("<a ")) continue;
          data.strSupp = append(data.strSupp, "\n", line);
        }
        return true;
      }

      return false;
    } while (false);

    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  // Address field must parse : @<place name> syntax
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strPlace = p.getLastOptional(": @");
      super.parse(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern TIME_PTN = Pattern.compile("\\d\\d?:\\d\\d( [AP]M)?");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      if (match.group(1) == null) {
        data.strTime = field;
      } else {
        setTime(TIME_FMT, field, data);
      }
    }
  }

  // INFO field may have cell phone # & GPS location
  private static final Pattern CELL_INFO_PTN =
      Pattern.compile("^ALT# ([\\d\\-]+) ([+\\-]\\d+\\.\\d+ [+\\-]\\d+\\.\\d+), *");
  private class MyInfoField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CELL_INFO_PTN.matcher(field);
      if (match.find()) {
        data.strPhone = match.group(1);
        setGPSLoc(match.group(2), data);
        field = field.substring(match.end()).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PHONE GPS INFO";
    }
  }
}
