package net.anei.cadpage.parsers.WA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class WABentonCountyParser extends FieldProgramParser {

  public WABentonCountyParser() {
    super(CITY_CODES, "BENTON COUNTY", "WA", 
          "Location:ADDR/S46! X-St:X! &:X! Disp:UNIT");
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
    };
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    
    // A text body of exactly 150 characters has probably been split
    data.expectMore = body.length() == 150;
    
    if (body.startsWith(":")) body = "Location" + body;
    else if (!body.startsWith("Location:")) body = "Location:" + body;
    
    if (!super.parseMsg(body, data)) return false;
    if (data.strAddress.length() == 0) {
      parseAddress(data.strCross, data);
      data.strCross = "";
    }
    return true;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private static Pattern ADDR = Pattern.compile("(.*?)(?:,(.*?))?(?: : *@?(.*))?(\\d{2}:\\d{2}:\\d{2})(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = ADDR.matcher(field);
      if (!mat.matches()) abort();
      super.parse(mat.group(1).trim(), data);
      data.strApt = append(data.strApt, "-", getOptGroup(mat.group(2)));
      data.strPlace = getOptGroup(mat.group(3));
      data.strTime = mat.group(4);
      data.strCall = mat.group(5).trim();
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT PLACE TIME CALL";
    }
  }
  
  private static Properties CITY_CODES = buildCodeTable(new String[] {
      "BNCT BENT", "BENTON CITY",
      "BNCO BENT", "BENTON COUNTY",
      "KENN BENT", "KENNEWICK",
      "RICH BENT", "RICHLAND",
      "WRCH BENT", "WEST RICHLAND"
  });

}
