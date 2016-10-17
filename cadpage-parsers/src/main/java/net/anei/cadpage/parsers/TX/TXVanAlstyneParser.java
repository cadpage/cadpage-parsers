package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXVanAlstyneParser extends FieldProgramParser {

  public TXVanAlstyneParser() {
    super("VAN ALSTYNE", "TX", "TYPE:CALL! LOC:ADDRCITY! TXT:INFO? INFO+");
  }

  private static final Pattern MULTISPACE = Pattern.compile(" {2,}");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    // check subj
    if (!subject.equals("CAD")) return false;
    
    return parseFields(body.split("\n"), data);
  }

  private static final Pattern D_MI = Pattern.compile("(.*? )\\d+(?:/\\d+)? M(?:I(?:LES?)?)? ([NSEW])[THOSAERU]*( OF .*)");
  
  @Override  public String adjustMapAddress(String sAddress) {
    //special case, its park not pike
    sAddress = sAddress.replace("HERITAGE PK", "HERITAGE PARK");
    //adjust address so "[NSEW] OF" intersections will parse (time consuming... worth it?)
    Matcher mat = D_MI.matcher(sAddress);
    if (mat.matches()) sAddress = mat.group(1) + mat.group(2) + mat.group(3);
    return super.adjustMapAddress(sAddress);
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {

    @Override
    public void parse(String field, Data data) {
      int si = field.indexOf('/');
      if (si == -1) abort();
      data.strCode = field.substring(0, si);
      data.strCall = field.substring(si + 1);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class MyAddressCityField extends AddressCityField {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.getLastOptional(',');
      if (data.strCity.length() == 0) data.strCity = p.getLastOptional("  ");
      if (data.strCity.length() == 0) abort();
      data.strPlace = p.getLastOptional('@');
      String addr = p.get();
      addr = MULTISPACE.matcher(addr).replaceAll(" ");
      parseAddress(addr, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR X APT CITY PLACE";
    }
  }
}
