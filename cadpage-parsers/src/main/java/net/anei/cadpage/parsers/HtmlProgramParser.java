package net.anei.cadpage.parsers;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class HtmlProgramParser extends FieldProgramParser {
  
  private HtmlDecoder decoder;
  
  /* HtmlParser Constructors */
  public HtmlProgramParser(String defCity, String defState, String prog) {
    this(defCity, defState, prog, null);
  }
  
  public HtmlProgramParser(String defCity, String defState, String prog, String userTags) {
    super(defCity, defState, prog);
    decoder = new HtmlDecoder(userTags);
  }
  
  public HtmlProgramParser(String[] cityList, String defCity, String defState, String prog) {
    this(cityList, defCity, defState, prog, null);
  }
  
  public HtmlProgramParser(String[] cityList, String defCity, String defState, String prog, String userTags) {
    super(cityList, defCity, defState, prog);
    decoder = new HtmlDecoder(userTags);
  }
  
  public HtmlProgramParser(Properties cityCode, String defCity, String defState, String prog) {
    this(cityCode, defCity, defState, prog, null);
  }
  
  public HtmlProgramParser(Properties cityCode, String defCity, String defState, String prog, String userTags) {
    super(cityCode, defCity, defState, prog);
    decoder = new HtmlDecoder(userTags);
  }
  
  public void setPreserveWhitespace(boolean preserveWhiteSpace) {
    decoder.setPreserveWhitespace(preserveWhiteSpace);
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    String[] flds = decoder.parseHtml(body);
    if (flds == null) return false;
    return parseFields(flds, data);
  }
}
