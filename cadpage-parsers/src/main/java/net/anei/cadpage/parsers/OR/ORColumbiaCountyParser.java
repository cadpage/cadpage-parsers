package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORColumbiaCountyParser extends FieldProgramParser {

  public ORColumbiaCountyParser() {
    super("COLUMBIA COUNTY", "OR", 
          "CALL! ( ADDR_TIME | ADDR! PLACE? ( CH! MAP_TIME! | MAP_TIME! ) ) X:X? INFO/N+");
  }

  private static Pattern ID_PTN = Pattern.compile("([A-Z]{0,2}\\d{8,9}) +");
  private static Pattern MUTUAL_AID_FORMAT = Pattern.compile("([,A-Z0-9]+) (mutual aid to [A-Z0-9]+) at (.*?)(?:; *(.*?))? (for .*?)\\.");
  private static Pattern BRK_PTN = Pattern.compile("\\[\\d\\]");

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Strip off optional leading ID
    Matcher match = ID_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strCallId = match.group(1);
      body = body.substring(match.end());
    }
    
    // "mutual aid to" format
    Matcher mafMat = MUTUAL_AID_FORMAT.matcher(body);
    if (mafMat.matches()) {
      data.strUnit = mafMat.group(1);
      data.strCall = mafMat.group(2);
      parseAddress(mafMat.group(3).trim(), data);
      data.strPlace = getOptGroup(mafMat.group(4));
      String g5 = mafMat.group(5);
      if (g5.length() > 4) data.strSupp = mafMat.group(5); //min length is 4. ("for ")
      setFieldList("UNIT CALL ADDR APT CITY PLACE INFO");
      return true;
    }

    if (!body.contains("\n")) {
      body = SP_CITY_PTN.matcher(body).replaceFirst("\n");
      body = body.replace(" Map ", "\nMap ").replace(" X:", "\nX:");
      body = BRK_PTN.matcher(body).replaceAll("\n");
    }
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " +  super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ADDR_TIME")) return new MyAddressTimeField();
    if (name.equals("MAP_TIME")) return new MyMapTimeField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CH")) return new ChannelField("TAC.*", true);
    if (name.equals("PLACE")) return new PlaceField();
    return super.getField(name);
  }

  private static Pattern CALL_UNIT = Pattern.compile("(.+?) +([^ ]+)");

  private class MyCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher lwMat = CALL_UNIT.matcher(field);
      if (!lwMat.matches()) abort();
      data.strCall = lwMat.group(1);
      data.strUnit = lwMat.group(2).replace(",", " ");
    }

    @Override
    public String getFieldNames() {
      return "CALL UNIT";
    }

  }

  private static Pattern CITY_CODE = Pattern.compile("(?:([ A-Z]*?) *;)? *(.*?)");

  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      Matcher ccMat = CITY_CODE.matcher(field);
      if (!ccMat.matches()) abort();
      // City
      String g1 = ccMat.group(1);
      if (g1 != null) data.strCity = convertCodes(g1, CITY_CODES);
      // Address
      parseAddress(ccMat.group(2), data);
    }

    @Override
    public String getFieldNames() {
      return "CITY ADDR APT";
    }
  }
  
  private static final Pattern ADDR_TIME_PTN = Pattern.compile("(.*) (\\d{2}:\\d{2}:\\d{2})");
  private class MyAddressTimeField extends MyAddressField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ADDR_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strTime = match.group(2);
      super.parse(match.group(1).trim(), data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " TIME";
    }
  }

  private static Pattern MAP_TIME = Pattern.compile("(?:Map )?(.*?) +(\\d{2}:\\d{2}:\\d{2})|Map +(.*)");

  private class MyMapTimeField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher mtMat = MAP_TIME.matcher(field);
      if (!mtMat.matches()) return false;
      String map = mtMat.group(1);
      if (map != null) {
        data.strMap = map.trim();;
        data.strTime = mtMat.group(2);
      } else {
        data.strMap = mtMat.group(3);
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "MAP TIME";
    }

  }

  //Replace , with /
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replaceAll("No X Street", "");
      field = field.replace(',', '/');
      field = stripFieldEnd(field, "/");
      field = stripFieldStart(field, "/");
      super.parse(field, data);
    }
  }
  
  private static final Pattern SP_CITY_PTN = Pattern.compile("[, ](?=COLUMBIA CITY|CLATSKAINE|CLATSKANIE|COLUMBIA|MULTNOMAH|PRESCOTT|RAINIER|SCAPPOOSE|ST HELENS|DEER ISLAND|VERNONIA|PORTLAND|WARREN)");
  
  //All codes excluding COL, MUL, and SCA are conjectures based on the format of the aforementioned three
  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "CC",  "COLUMBIA CITY",
      "CLA", "CLATSKAINE",
      "CLT", "CLATSKANIE",
      "COL", "COLUMBIA",
      "MUL", "MULTNOMAH",
      "PRE", "PRESCOTT",
      "RAI", "RAINIER",
      "SCA", "SCAPPOOSE",
      "SH",  "ST HELENS",
      "VER", "VERNONIA",
      "WCA", "WASHINGTON COUNTY"
  });
  
}
