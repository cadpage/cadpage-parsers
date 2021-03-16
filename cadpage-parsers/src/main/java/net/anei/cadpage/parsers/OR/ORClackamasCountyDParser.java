package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class ORClackamasCountyDParser extends FieldProgramParser {

  public ORClackamasCountyDParser() {
    this("CLACKAMAS COUNTY", "OR");
  }

  public ORClackamasCountyDParser(String defCity, String defState) {
    super(defCity, defState,
          "CH ID CALL TIME ADDR PLACE MAP MAP/S MAP/S X UNIT INFO! INFO/N+ Sent_by:SKIP!");
  }
  
  @Override
  public String getAliasCode() {
    return "ORClackamasCountyD";
  }

  @Override
  public String getFilter() {
    return "@portlandoregon.gov";
  }

  private static final Pattern MASTER1
    = Pattern.compile("(?:(OPS\\d) )?(R[CPG] #\\d+) (.*?) (\\d\\d:\\d\\d:\\d\\d) (.*?) ([FGM][A-Z] [A-Z]?\\d{2,4} \\d{4}[A-Z0-9]) (?:(.*?) )?DISPATCHED: (.*) REMARKS: *(.*?)(?: Sent by: (\\S+))?");
  private static final Pattern MASTER2
  = Pattern.compile("(.+?) (\\d\\d:\\d\\d:\\d\\d) (.*?) ([FGM][A-Z] [A-Z]?\\d{2,4} \\d{4}[A-Z0-9]) (?:(.*?) )?(P[GM] #\\d+) REMARKS: *(.*?)(?: Sent by: (\\S+))?");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("; ")) {
      return parseFields(body.substring(2).split("; "), data);
    }

    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("CH ID CODE CALL TIME APT ADDR PLACE MAP X UNIT INFO SRC");
      data.strChannel = getOptGroup(match.group(1));
      data.strCallId = match.group(2);
      parseCodeCall(match.group(3).trim(), data);
      data.strTime = match.group(4);
      parseLocation(match.group(5).trim(), data);
      data.strMap = match.group(6);
      data.strCross = getOptGroup(match.group(7));
      data.strUnit = match.group(8).trim();
      data.strSupp = match.group(9).trim();
      data.strSource = getOptGroup(match.group(10));
      return true;
    }

    match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("CODE CALL TIME APT ADDR MAP X ID INFO SRC");
      parseCodeCall(match.group(1).trim(), data);
      data.strTime = match.group(2);
      parseLocation(match.group(3).trim(), data);
      data.strMap = match.group(4);
      data.strCross = getOptGroup(match.group(5));
      data.strCallId = match.group(6).trim();
      data.strSupp = match.group(7).trim();
      data.strSource = getOptGroup(match.group(8));
      return true;
    }

    return false;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CH")) return new ChannelField("OPS\\d", true);
    if (name.equals("ID")) return new IdField("R[CGP] #\\d+", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      parseCodeCall(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      parseLocation(field, data);
    }
  }

  private void parseCodeCall(String field, Data data) {
    int pt = field.indexOf(" - ");
    if (pt >= 0) {
      data.strCode = field.substring(0,pt).trim();
      data.strCall = field.substring(pt+3).trim();
    } else {
      data.strCall = field;
    }
  }

  private static final Pattern APT_ADDR_PTN = Pattern.compile("(\\S+)-(\\d+ .*)");

  private void parseLocation(String field, Data data) {
    String apt = "";
    Matcher match = APT_ADDR_PTN.matcher(field);
    if (match.matches()) {
      apt = match.group(1);
      field = match.group(2);
    }
    int pt = field.indexOf('[');
    if (pt >= 0) {
      data.strPlace = field.substring(pt+1).trim();
      parseAddress(field.substring(0,pt).trim(), data);
    } else {
      parseAddress(StartType.START_ADDR, field, data);
      data.strPlace = getLeft();
      if (data.strPlace.startsWith("&")) {
        data.strAddress = append(data.strAddress, " ", data.strPlace);
        data.strPlace = "";
      }
    }
    data.strApt = append(apt, "-", data.strApt);
  }
}
