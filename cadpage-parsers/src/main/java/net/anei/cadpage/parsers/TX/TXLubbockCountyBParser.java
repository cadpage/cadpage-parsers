package net.anei.cadpage.parsers.TX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXLubbockCountyBParser extends FieldProgramParser {
    
  public TXLubbockCountyBParser() {
    super("LUBBOCK COUNTY", "TX",
          "ADDR CODE_CALL! CALL+? INFO+? ID");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@umchealthsystem.com";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("RC:")) return false;
    body = body.substring(3).trim();
    return parseFields(body.split("/"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID")) return new IdField("Run# *(\\d+)", true);
    return super.getField(name);
  }
  
  private static final Pattern CALL_CODE_PTN = Pattern.compile("(\\d{1,2}-[A-Z]-\\d{1,2}) (.*)");
  private class MyCodeCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }
  
  private static final Pattern TRAIL_Y_PTN = Pattern.compile("\\bY$", Pattern.CASE_INSENSITIVE);
  private static final Pattern LEAD_O_PTN = Pattern.compile("^O\\b", Pattern.CASE_INSENSITIVE);
  private class MyCallField extends CallField {
    
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!data.strCall.endsWith(" w") && !data.strCall.endsWith(" W") &&
          !(TRAIL_Y_PTN.matcher(data.strCall).find() && LEAD_O_PTN.matcher(field).find()) &&
          !(data.strCall.endsWith(" y") && field.startsWith("o ")) &&
          !CALL_CONT_SET.contains(field)) return false;
      data.strCall = append(data.strCall, "/", field);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "ProQA comments:");
      field = field.replace("ProQA comments:", "/");
      super.parse(field, data);
    }
  }
  
  private static final Set<String> CALL_CONT_SET = new HashSet<String>(Arrays.asList(
      "Childbirth",
      "Chills",
      "Envenomations",
      "Fainting (Near)",
      "ill",
      "Miscarriage",
      "Seizures",
      "Stings",
      "Transportation Incidents",
      "Vertigo"
  ));
}
