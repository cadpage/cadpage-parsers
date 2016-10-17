package net.anei.cadpage.parsers.GA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class GACoffeeCountyParser extends DispatchB2Parser {

  public GACoffeeCountyParser() {
    super("COFFEE911:||911-CENTER:", "COFFEE COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "911-CENTER@coffeecountygov.com,COFFEE911@coffeecountygov.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" OLD BELL LK ", " OLD BELL LAKE RD ");
    return super.parseMsg(body, data);
  }
  
  @Override
  public String adjustMapAddress(String address) {
    return MLK_PTN.matcher(address).replaceAll("MARTIN LUTHER KING");
  }
  private static final Pattern MLK_PTN = Pattern.compile("\\bM *L *K\\b");
  
}
