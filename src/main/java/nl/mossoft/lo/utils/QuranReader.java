/*
 * This file is part of QuranLO
 *
 * Copyright (C) 2020 <mossie@mossoft.nl>
 *
 * QuranLO is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <https://www.gnu.org/licenses/>.
 */

package nl.mossoft.lo.utils;

import com.sun.star.deployment.PackageInformationProvider;
import com.sun.star.deployment.XPackageInformationProvider;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XURLTransformer;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Helper Class for reading Qur'an text from xml source.
 *
 * @author abdullah
 */
public class QuranReader {

  private static final String QURAN_RESOURCES = "data/quran/";
  private Document doc;
  private XPath xpath = null;

  /**
   * Creates a document reader for the Qur'an xml files.
   *
   * @param language the language of the Qur'an txt
   * @param version  the version
   * @param context  the document context.
   */
  public QuranReader(final String language, final String version, final XComponentContext context) {
    try {
      final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
      df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
      df.setNamespaceAware(true);

      final DocumentBuilder builder = df.newDocumentBuilder();
      doc = builder.parse(getQuranFile(getFilename(language, version), context));

      // Create XPathFactory object
      final XPathFactory xpathFactory = XPathFactory.newInstance();

      // Create XPath object
      xpath = xpathFactory.newXPath();

    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns a file path for a file in the installed extension, or null on failure.
   */
  public static File getFilePath(final String file, final XComponentContext xcontext) {
    final XPackageInformationProvider xpackageInformationProvider = PackageInformationProvider.get(
        xcontext);
    final String location = xpackageInformationProvider.getPackageLocation(
        "nl.mossoft.lo.QuranLOAddon");
    Object otransformer;
    try {
      otransformer = xcontext.getServiceManager()
          .createInstanceWithContext("com.sun.star.util.URLTransformer", xcontext);
    } catch (final Exception e) {
      e.printStackTrace();
      return null;
    }
    final XURLTransformer xtransformer = UnoRuntime.queryInterface(XURLTransformer.class,
        otransformer);
    final com.sun.star.util.URL[] ourl = new com.sun.star.util.URL[1];
    ourl[0] = new com.sun.star.util.URL();
    ourl[0].Complete = location + "/" + file;
    xtransformer.parseStrict(ourl);
    URL url;
    try {
      url = new URL(ourl[0].Complete);
    } catch (final MalformedURLException e1) {
      return null;
    }
    File f;
    try {
      f = new File(url.toURI());
    } catch (final URISyntaxException e1) {
      return null;
    }
    return f;
  }

  public static String[] getQuranVersions(String language, boolean include) {

    return null;
  }

  /**
   * Provides the name of a surah based on its number.
   *
   * @param surahno number of surah
   * @return name of surah
   */
  public static String getSurahName(final int surahno) {
    final String[] surahs = new String[]{"Al-F??tihah", "Al-Baqarah", "??l-`Imr??n", "An-Nis??",
        "Al-M??`idah", "Al-An`??m", "Al-A`r??f", "Al-Anf??l", "At-Tawbah", "Y??nus", "H??d", "Y??suf",
        "Ar-Ra`d", "Ibr??him", "Al-Hijr", "An-Nahl", "Al-Isr??`", "Al-Kahf", "Maryam", "T??-H??",
        "Al-Anbiy??", "Al-Hajj", "Al-Mu`min??m", "An-N??r", "Al-Furq??n", "Ash-Shu`ar??`", "Al-Naml",
        "Al-Qa???a???", "Al-Ankab??t", "Ar-R??m", "Luqm??n", "As-Sajdah", "Al-Ahz??b", "Saba`", "F?????ir",
        "Y??-S??n", "As-?????ff??t", "?????d", "Az-Zumar", "Gh??fir", "Fu???silat", "Ash-Sh??ra", "Az-Zukhruf",
        "Ad-Dukh??n", "Al-Dj??thiyah", "Al-A???q??f", "Mu???ammad", "Al-Fat???", "Al-???ujur??t", "Q??f",
        "Adh-Dh??riy??t", "At-?????r", "An-Najm", "Al-Qamar", "Ar-Ra???m??n", "Al-W??qi`ah", "Al-???ad??d",
        "Al-Mudj??dilah", "Al-???ashr",
        "Al-Mumtahana", "As-Saf", "Al-Jumu'ah", "Al-Munafiqun", "At-Taghabun", "At-Talaq",
        "At-Tahrim", "Al-Mulk", "Al-Qalam", "Al-Haqqah", "Al-Ma'arij", "Al-Nuh", "Al-Jinn",
        "Al-Muzzammil", "Al-Muddathir", "Al-Qiyamah", "Al-Insan", "Al-Mursalat", "Al-Naba'",
        "Al-Nazi'at", "'Abasa", "At-Takwir", "Al-Infitar", "Al-Mutaffifin", "Al-Inshiqaq",
        "Al-Buruj", "At-Tariq", "Al-A'la", "Al-Ghashiyah", "Al-Fajr", "Al-Balad", "Ash-Shams",
        "Al-Layl", "Ad-Dhuhaa", "Al-Sharh", "At-Tin", "Al-Alaq", "Al-Qadr", "Al-Bayyinah",
        "Az-Zalzalah", "Al-Adiyat", "Al-Qari'ah", "At-Takathur", "Al-Asr", "Al-Humazah", "Al-Fil",
        "Quraysh", "Al-Ma'un", "Al-Kawthar", "Al-Kafirun", "An-Nasr", "Al-Masad", "Al-Ikhlas",
        "Al-Falaq", "An-Nas"};
    return surahs[surahno - 1];
  }

  /**
   * Returns the number of ayats in a surah.
   *
   * @param surahno the number of the surah
   * @return the number
   */
  public static long getSurahSize(final int surahno) {
    final long[] surahSizes = new long[]{7, 286, 200, 176, 120, 165, 206, 75, 129, 109, 123, 111,
        43, 52, 99, 128, 111, 110, 98,
        135, 112, 78, 118, 64, 77, 227, 93, 88, 69, 60, 34, 30, 73, 54, 45, 83, 182, 88, 75, 85,
        54, 53, 89, 59, 37, 35, 38, 29, 18, 45, 60, 49, 62, 55, 78, 96, 29, 22, 24, 13, 14, 11, 11,
        18, 12, 12, 30, 52, 52, 44, 28, 28, 20, 56, 40, 31, 50, 40, 46, 42, 29, 19, 36, 25, 22, 17,
        19, 26, 30, 20, 15, 21, 11, 8, 8, 19, 5, 8, 8, 11, 11, 8, 3, 9, 5, 4, 7, 3, 6, 3, 5, 4, 5,
        6};
    return surahSizes[surahno - 1];
  }

  /**
   * Get the filename for a particular quran version.
   *
   * @param language the language of the version
   * @param version  a version
   * @return the filename
   */
  private static String getFilename(final String language, final String version) {
    return "QuranText." + language + "." + version + ".xml";
  }

  /**
   * Returns a path to a Quran file.
   */
  static File getQuranFile(final String xdlFile, final XComponentContext xcontext) {
    return getFilePath(QURAN_RESOURCES + xdlFile, xcontext);
  }

  /**
   * Read all the ayat of a surah from the xml source file.
   *
   * @param surano the surah
   * @return list of ayat.
   */
  public List<String> getAllAyatOfSuraNo(final int surano) {
    final List<String> list = new ArrayList<>();
    try {
      final XPathExpression expr1 = xpath.compile("/quran/surah[@no='" + surano + "']/ayat/@text");
      final NodeList nodes = (NodeList) expr1.evaluate(doc, XPathConstants.NODESET);
      for (int i = 0; i < nodes.getLength(); i++) {
        list.add(nodes.item(i).getNodeValue());
      }
    } catch (final XPathExpressionException e) {
      e.printStackTrace();
    }
    return list;
  }

  /**
   * Get an ayat from a surah.
   *
   * @param surano the surah
   * @param ayano  the ayah
   * @return ayah
   */
  public String getAyahNoOfSuraNo(final int surano, final long ayano) {
    String aya = null;
    try {
      final XPathExpression expr = xpath.compile(
          "/quran/surah[@no='" + surano + "']/ayat[@no='" + ayano + "']/@text");
      aya = (String) expr.evaluate(doc, XPathConstants.STRING);
    } catch (final XPathExpressionException e) {
      e.printStackTrace();
    }
    return aya;
  }

  /**
   * Get a range of ayat from a surah.
   *
   * @param surano  the surah
   * @param ayafrom the start ayah
   * @param ayato   the last ayah
   * @return list of ayaht
   */
  public List<String> getAyatFromToOfSuraNo(
      final int surano, final long ayafrom, final long ayato) {
    final List<String> list = new ArrayList<>();
    for (long ayano = ayafrom; ayano <= ayato; ayano++) {
      list.add(getAyahNoOfSuraNo(surano, ayano));
    }
    return list;
  }

  /**
   * Get Bismillah.
   *
   * @return Bismillah
   */
  public String getBismillah() {
    String bismillah = null;
    try {
      final XPathExpression expr = xpath.compile("/quran/surah[@no='1']/ayat[@no='1']/@text");
      bismillah = (String) expr.evaluate(doc, XPathConstants.STRING);
    } catch (final XPathExpressionException e) {
      e.printStackTrace();
    }
    return bismillah;
  }


}
