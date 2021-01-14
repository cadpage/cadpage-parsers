package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;



public class SCYorkCountyParser extends FieldProgramParser {

  public SCYorkCountyParser() {
    super("YORK COUNTY", "SC",
          "( SELECT/4 UNIT CALL ADDR! " +
          "| SELECT/3 ADDRCITYST!" +
          "| SELECT/2 UNIT_CH_CALL! CALL2! LOC:ADDRCITYST! NAR:INFO+ " +
          "| UNIT! P:PRI_CH_CALL! CALL2! LOC:ADDRCITYST! X:X! NAR:INFO+ " +
          ") INC#:ID! GPS END");
  }

  @Override
  public String getFilter() {
    return "paging@yorkcountygov.com";
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("([-A-Z0-9]+) TIMES / ([^/]+?) / INCIDENT (\\d+) / (.*)");
  private static final Pattern PREFIX_PTN = Pattern.compile("(?:((?:Initial|2nd|3rd|4th) Dispatch)|(Short Report)|(Call Complete)|(UC/Cancel Further Response))[- ]*");
  private static final Pattern DELIM = Pattern.compile("\\*+| +(?=P:|X:|NAR:|INC#:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT ADDR ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strUnit = match.group(1);
      parseAddress(match.group(2).trim(), data);
      data.strCallId = match.group(3);
      data.strSupp = match.group(4).trim().replace(" / ", "\n");
      return true;
    }

    match = PREFIX_PTN.matcher(body);
    if (match.lookingAt()) {
      body = body.substring(match.end());
    } else {
      match = PREFIX_PTN.matcher(subject);
      if (!match.matches()) return false;
    }

    int type = 1;
    while (true) {
      if (match.group(type) != null) break;
      if (++type > 4) return false;
    }

    setSelectValue(Integer.toString(type));
    if (type == 3) data.msgType = MsgType.RUN_REPORT;
    if (!parseFields(DELIM.split(body), data)) return false;
    if (type == 4) data.strCall = append("CANCEL", " - ", data.strCall);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("UNIT_CH_CALL")) return new MyUnitChannelCallField();
    if (name.equals("PRI_CH_CALL")) return new MyPriorityChannelCallField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern UNIT_DELIM_PTN = Pattern.compile(" *; *");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_DELIM_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }

  private static final String CH_CALL_PTN_STR = "(?:(?i:None|(?:([A-Z ]+?)[/ ]+)?((?:(?:COMM|RES)[/ ]+)?TAC *\\d*(?:COMM)?))[/ ]+)?(.*)";
  private static final Pattern UNIT_CH_CALL_PTN = Pattern.compile("([-A-Z0-9]+(?:; *[-A-Z0-9]+)*?) +" + CH_CALL_PTN_STR);
  private class MyUnitChannelCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = UNIT_CH_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strUnit = UNIT_DELIM_PTN.matcher(match.group(1)).replaceAll(",");
      String callExt = match.group(2);
      data.strChannel = getOptGroup(match.group(3));
      data.strCall = match.group(4);
      if (callExt != null) data.strCall = append(data.strCall, " - ", callExt);
    }

    @Override
    public String getFieldNames() {
      return "UNIT CH CALL";
    }
  }

  private static final Pattern PRI_CH_CALL_PTN = Pattern.compile("(\\d|None) +" + CH_CALL_PTN_STR);
  private class MyPriorityChannelCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = PRI_CH_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strPriority = match.group(1);
      String callExt = match.group(2);
      data.strChannel = getOptGroup(match.group(3));
      data.strCall = match.group(4);
      if (callExt != null) data.strCall = append(data.strCall, " - ", callExt);
    }

    @Override
    public String getFieldNames() {
      return "PRI CH CALL";
    }
  }

  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String delim = " * ";
      for (String part : field.split(";")) {
        data.strSupp = append(data.strSupp, delim, part.trim());
        delim = "\n";
      }
    }
  }
}
