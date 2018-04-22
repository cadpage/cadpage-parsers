package net.anei.cadpage.parsers.AR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class ARSebastianCountyParser extends FieldProgramParser {
  
  public ARSebastianCountyParser() {
    super("SEBASTIAN COUNTY", "AR", 
          "CALL:CODE_CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! DATE:DATE/d! TIME:TIME! UNIT:UNIT% INFO:INFO% INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "cad@fsems.org";
  }
  
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }
  
  private static final Pattern MASTER = Pattern.compile("([A-Za-z ]+?) respond to ([^,]*?)(?:, ([^,]*?))?(?:, ([A-Z]{2}) +\\d{5})?(?: *([^,]*?))? for (?:(\\d) )?(.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("SRC ADDR CITY ST APT PRI CALL");
      data.strSource = match.group(1).trim();
      parseAddress(match.group(2).trim(), data);
      data.strCity = getOptGroup(match.group(3));
      data.strState = getOptGroup(match.group(4));
      data.strApt = append(data.strApt, "-", getOptGroup(match.group(5)));
      data.strPriority = getOptGroup(match.group(6));
      data.strCall = match.group(7).trim();
      return true;
    }
    
    
    if (body.length() >= 243 && body.length() <= 265) data.expectMore = true;
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PRI")) return new MyPriorityField();
    if (name.equals("DATE")) return new DateField("\\d\\d-\\d\\d-\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d");
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\S+) - (.*)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        data.strCall = match.group(2).trim();
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +\\d{5})?");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      Parser p = new Parser(field);
      String city = p.getLastOptional(",");
      Matcher match = ADDR_ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        city = p.getLastOptional(',');
      }
      data.strCity = city;
      super.parse(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }
  
  private class MyPriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Priority");
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
}
