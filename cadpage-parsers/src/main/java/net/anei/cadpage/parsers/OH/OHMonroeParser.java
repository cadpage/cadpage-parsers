package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHMonroeParser extends FieldProgramParser {

  public OHMonroeParser() {
    super(CITY_CODES, "MONROE", "OH", 
          "ID CALL ADDRCITY INFO X! Loc_Info:INFO! Owner:PLACE! Time_Out:DATETIME");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@monroeohio.org";
  }
  
  @Override
  public String adjustMapAddress(String sAddress, boolean cross) {
    sAddress = sAddress.replace("HAM LEB RD", "HAMILTON LEBANON RD");
    sAddress = sAddress.replace("HAM MIDD RD", "HAMILTON MIDDLETOWN RD");
    //guessing acronyms now, there might be more roads too
    sAddress = sAddress.replace("HAM TRENT RD", "HAMILTON MIDDLETOWN RD");
    return sAddress;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //check subj
    if (!subject.startsWith("CAD Page")) return false;
    
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2}-\\d{6}", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  public class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      //trim outside |'s
      field = stripFieldStart(field, "|");
      field = stripFieldEnd(field, "|");
      //replace | with - and parse
      field = field.replace("|", "-");
      super.parse(field, data);
    }
  }
  
  public class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("@", "&");
      super.parse(field, data);
    }
  }
  
  public static Pattern APT = Pattern.compile("^APT ([^ ]+)$");
  public class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      //remove trailing / or -
      field = stripFieldEnd(field, "-");
      field = stripFieldEnd(field, "/");

      //check for APT
      Matcher mat = APT.matcher(field);
      if (mat.matches()) {
        data.strApt = mat.group(1);
        return;
      } //otherwise parse normally
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "APT INFO";
    }
  }
  
  public class MyCrossField extends CrossField {
    public MyCrossField() {
      super("X of (.*)", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace("|&", "&").trim();
      //remove leading/trailing &
      field = stripFieldStart(field, "&");
      field = stripFieldEnd(field, "&");
      super.parse(field, data);
    }
  }
  
  public static Pattern DATETIME = Pattern.compile("(\\d+/\\d+/\\d{4})@(\\d{2}:\\d{2}:\\d{2})");
  public class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = DATETIME.matcher(field);
      if (!mat.matches()) abort();
      data.strDate = mat.group(1);
      data.strTime = mat.group(2);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "MIDDLE",   "MIDDLETON"
  });
}
