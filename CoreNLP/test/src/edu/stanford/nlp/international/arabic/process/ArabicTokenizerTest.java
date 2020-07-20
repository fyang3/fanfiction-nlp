package edu.stanford.nlp.international.arabic.process;

import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.process.TokenizerFactory;
import junit.framework.TestCase;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.Tokenizer;

/**
 * Tests ArabicTokenizer using 50 sentences from the vocalized section
 * of the ATB.
 *
 * @author Spence Green
 *
 */
public class ArabicTokenizerTest extends TestCase {

  private final String[] untokInputs = {
      "وَ- -كان+َ مِن ال+مُقَرَّر+ِ أَن يُ+شارِك+َ فِي ال+دَوْر+َة+ِ أَيْض+اً فَرِيق+ا ال+رِياضِيّ ال+لُبْنانِيّ+ُ وَ- -ال+خَلِيج ال+سَعُودِيّ+ُ لٰكِنَّ- -هُما ٱِعْتَذَر+ا فِي وَقْت+ٍ مُتَأَخِّر+ٍ مِن مَساء+ِ ال+يَوْم+ِ ال+ثُلاثاء+ِ .",
      "تَ+سْتَعِدّ+ُ ال+وِلاي+ات+ُ ال+مُتَّحِد+َة+ُ لِ- -حَرْب+ٍ دُوَلِيّ+َة+ٍ ضِدّ+َ ال+إِرْهاب+ِ سَ- -تُ+نْفِق+ُ عَلَي- -ها 40 بِلْيُون+ِ دُولار+ٍ .",
      "غوريل يَ+نْتَقِد+ُ خُطّ+َة+َ عَنان لِ- -تَوْحِيد+ِ ال+جَزِير+َة+ِ",
      "وَ- -رَدّ+َ شاهرودي : \" يُ+مَثِّل+ُ لُبْنان+ُ ال+خَنْدَق+َ ال+أَوَّل+َ الَّذِي يُ+واجِه+ُ ال+عُدْوان+َ ال+صَهْيُونِيّ+َ وَ- -يَ+خْتَزِن+ُ شَعْب+اً طَيِّب+اً وَ- -خَيِّر+اً ( . . . ) \" .",
      "وَ- -قَد عُثِر+َ عَلَى جُثّ+َت+ِ- -هِ أَسْفَل+َ جِسْر+ٍ عَلَى ال+طَرِيق+ِ ال+سَرِيع+ِ فِي بَلْد+َة+ِ فوسانو , وَ- -كان+َت سَيّار+َت+ُ- -هُ مُتَوَقِّف+َة+ً عَلَى ال+جِسْر+ِ الَّذِي يَ+بْلُغ+ُ عُلُوّ+ُ- -هُ ثَمان+ِينَ مِتْر+اً , وَ- -أَشار+َت ال+شُرْط+َة+ُ إِلَى عَدَم+ِ عُثُور+ِ- -ها عَلَى أَيّ+ِ إِشار+َة+ٍ بِ- -وُجُود+ِ سَرِق+َة+ٍ أَو ٱِعْتِداء+ٍ .",
      "وَ- -كان+َ ال+وَباء+ُ نَفْس+ُ- -هُ أَوْدَى+[نُلل] بِ- -حَيا+َة+ِ 300 شَخْص+ٍ فِي ال+كُونْغُو عام+َ 1995 وَ- -حَوالَي خَمْس+ِينَ آخَر+ِينَ فِي ال+غابُون .",
      "وَ- -لَم تَ+نْسَ+[نُلل] ال+مُؤَلِّف+َة+ُ كلودين رَبْط+َ حَيا+َة+ِ آل+ِ تشابلن فِي ال+حَوادِث+ِ ال+تارِيخِيّ+َة+ِ الَّتِي رافَق+َت- -هُم فِي \" عاشِق+ا ال+زَمَن+ِ ال+مُعاصِر+ِ \" .",
      "أَعْلَن+َ رَئِيس+ُ إِنْدُونِيسِيا عَبْد ال+رَحْمٰن وَحِيد ال+يَوْم+َ ال+ثُلاثاء+َ أَنَّ- -هُ ٱِلْتَقَى+[نُلل] وَزِير+َ ال+تَعاوُن+ِ ال+إِقْلِيمِيّ+َ ال+إِسْرائِيلِيّ+َ شِيمُون بِيرِيز أَمْسِ , عَلَى أَن يَ+لْتَقِي+َ ال+رَئِيس+َ ال+فِلَسْطِينِيّ+َ ياسِر عَرَفات مَساء+َ ال+يَوْم+ِ .",
      "وَ- -بَعْد+َ ثَمانِي+َة+ِ أَيّام+ٍ مِن ال+ٱِنْتِخاب+ات+ِ ال+رِئاسِيّ+َة+ِ ال+أَكْثَر+ِ ٱِحْتِدام+اً فِي تارِيخ+ِ ال+وِلاي+ات+ِ ال+مُتَّحِد+َة+ِ لا يَ+زال+ُ ٱِنْتِخاب+ُ رَئِيس+ٍ جَدِيد+ٍ مَرْهُون+اً ال+يَوْم+َ ال+أَرْبِعاء+َ بِ- -ٱِحْتِمال+ِ مُواصَل+َة+ِ عَمَلِيّ+ات+ِ ال+فَرْز+ِ ال+يَدَوِيّ+َة+ِ فِي بَعْض+ِ مُقاطَع+ات+ِ وِلاي+َة+ِ فلُورِيدا حَيْثُ تُ+فِيد+ُ ال+نَتِيج+َة+ُ ال+أَوَّلِيّ+َة+ُ بِ- -أَنَّ جُورْج بُوش يَ+تَقَدَّم+ُ عَلَى مُنافِس+ِ- -هِ آل غُور بِ 300 صَوْت+ٍ .",
      "وَ- -ماذا عَلَى لُبْنان+َ , قِطاع+اً عامّ+اً وَ- -صِناعِيّ+اً مُنْتِج+اً أَن يَ+فْعَل+َ لِ- -مُواجَه+َة+ِ مَأْزِق+ِ عَدَم+ِ ال+قُدْر+َة+ِ مِن ال+إِفاد+َة+ِ , مِن ال+تَحَوُّل+ات+ِ ال+حاصِل+َة+ِ بِ- -ما يَ+ضْمَن+ُ ٱِسْتِقْرار+َ ٱِقْتِصاد+ات+ِ- -ها ال+وَطَنِيّ+َة+ِ ? \" .",
      "مَنَح+َ رَئِيس+ُ ال+جُمْهُورِيّ+َة+ِ إِمِيل لَحُود ال+رَئِيس+َ ال+سابِق+َ لِ- -مَجْلِس+ِ ال+قَضاء+ِ ال+أَعْلَى نَصْرِي لَحُود أَمْسِ فِي ال+قَصْر+ِ ال+جُمْهُورِيّ+ِ وِسام+َ ال+أَرْز+ِ ال+وَطَنِيّ+ِ مِن رُتْب+َة+ِ \" ضابِط+ٍ أَكْبَر+َ \" , لِ- -مُناسَب+َة+ِ ٱِنْتِهاء+ِ وِلاي+َت+ِ- -هِ فِي رِئاس+َة+ِ مَجْلِس+ِ ال+قَضاء+ِ .",
      "وَ- -قَد أُدْرِج+َ مُسابَق+َة+ُ ال+ترِياتْلُون رَسْمِيّ+اً فِي ال+أَلْعاب+ِ ال+أُولِمْبِيّ+َة+ِ لِ- -ال+مَرّ+َة+ِ ال+أُولَى .",
      "وَ- -مُذ ذاكَ , أُ+حاوِل+ُ أَن أَعْمَل+َ بِ- -هَدْي+ِ والِد- -ِي , ذاكِر+اً نَصِيح+َت+َ- -هُ عَلَى ال+دَوام+ِ , فَ- -ال+شَيْء+ُ ال+جَمِيل+ُ ال+نَبِيل+ُ يَ+جِب+ُ أَن يعمّم عَلَى ال+ناس+ِ , لا أَن يَ+بْقَى+[نُلل] طَيّ+َ ال+صُدُور+ِ وَ- -ال+قُلُوب+ِ .",
      "وَ- -إِدان+َة+ُ ال+حِزْب+ِ ال+شُيُوعِيّ+ِ لِ- -ال+إِرْهاب+ِ , لَم تَ+نْسَ ال+إِشار+َة+َ إِلَى أَنَّ \" ال+عَمَل+َ ال+إِرْهابِيّ+َ ثَمْر+َة+ٌ مُرّ+َة+ٌ لِ- -ال+سِياس+َة+ِ ال+أَمْرِيكِيّ+َة+ِ نَفْس+ِ- -ها ال+داخِلِيّ+َة+ِ وَ- -ال+خارِجِيّ+َة+ِ وَ- -لِ- -نَهْج+ِ ال+ٱِحْتِكار+ات+ِ ال+رَأْسمالِيّ+َة+ِ وَ- -دُوَل+ِ- -ها \" , مُتَوَقِّع+َة+ً \" سَ- -تَ+بْقَى ال+وِلاي+ات+ُ ال+مُتَّحِد+َة+ُ مُعَرَّض+َة+ً لِ- -ال+إِرْهاب+ِ \" .",
      "وَ- -قال+َ : \" لا بُدّ+َ مِن ال+ٱِنْتِقال+ِ بِ- -ال+إِعْلام+ِ ال+عَرَبِيّ+ِ مِن ال+ساح+َة+ِ ال+ضَيِّق+َة+ِ إِلَى ال+ساح+َة+ِ ال+دُوَلِيّ+َة+ِ لِ- -أَنَّ ال+مَرْحَل+َة+َ تَ+تَطَلَّب+ُ وَضْع+َ خُطّ+َة+ٍ عَرَبِيّ+َة+ٍ واحِد+َة+ٍ \" .",
      "وَ- -هٰذِهِ ال+مُداخَل+َة+ُ ال+أُولَى لِ- -رَئِيس+ِ ال+أَرْكان+ِ ال+إِسْرائِيلِيّ+ِ أَمام+َ هٰذا ال+مُنْتَدَى الَّذِي يَ+ضُمّ+ُ ضُبّاط+َ ال+أَرْكان+ِ وَ- -ال+ضُبّاط+َ ال+عامِل+ِينَ , مُنْذُ تَسَلُّم+ِ- -هِ مَسْؤُولِيّ+ات+ِ- -هِ فِي 9 تَمُّوز خَلَف+اً لِ- -ال+جِنِرال+ِ شاؤُول مُوفاز .",
      "وَ- -تُ+عْتَبَر+ُ ال+مُبارا+َة+ُ قِمّ+َة+َ ال+مَجْمُوع+َة+ِ ال+ثانِي+َة+ِ لٰكِنَّ ال+أَفْضَلِيّ+َة+َ بَعْد+َ أَداء+ِ ال+مُنْتَخَب+َيْنِ ال+جُمْع+َة+َ ال+ماضِي+َ تَ+صُبّ+ُ فِي مَصْلَح+َة+ِ ال+كُورِيّ+ِينَ , مِن حَيْثُ ال+جُهُوزِيّ+َة+ُ وَ- -ال+سُرْع+َة+ُ وَ- -ٱِسْتِثْمار+ُ ال+فُرَص+ِ , لٰكِن فِي كُر+َة+ِ ال+قَدَم+ِ لِ- -كُلّ+ِ مُبارا+َة+ٍ ظُرُوف+ُ- -ها وَ- -حِساب+ات+ُ- -ها ال+خاصّ+َة+ُ , وَ- -بِ- -ال+تالِي يَ+صْعُب+ُ ال+تَكَهُّن+ُ بِ- -ما سَ- -يَ+حْصُل+ُ غَد+اً لِ- -أَنَّ ال+عِمْلاق+َيْنِ ال+كُوَيْتِيّ+َ وَ- -ال+كُورِيّ+َ يَ+مْلِك+انِ جَمِيع+َ مُقَوِّم+ات+ِ ال+فَوْز+ِ .",
      "وَ- -ال+واقِع+ُ فِي ال+إِدار+َة+ِ ال+أَمِيرْكِيّ+َة+ِ ال+يَوْم+َ يُ+ثْبِت+ُ أَنَّ أَكْثَر+َ شَخْصِيّ+َة+ٍ لَ- -ها نُفُوذ+ٌ عَلَى بُوش هُوَ بِ- -حَسَب+ِ ال+صُحُف+ِ ال+أَمِيرْكِيّ+َة+ِ , دِيك تشِينِي .",
      "بِ \" قَبْر+ِ كوبران \" لِ- -رافيل ٱِفْتَتَح+َ لِقاء+َ- -هُ بِ- -ال+جُمْهُور+ِ ال+لُبْنانِيّ+ِ , وَ- -هِيَ توليفة مُرَكَّب+َة+ٌ مِن سِتّ+ِ مَقْطُوع+ات+ٍ كَرَّم+َ بِ- -ها رافيل ذِكْرَى كوبران ال+مُوسِيقِيّ فِي ال+قَرْن+ِ ال+سابِع+ِ عَشَر+َ وَ- -عَصْر+ِ ال+باروك الَّذِي عاش+َ فِي- -هِ .",
      "وَ- -هٰذا يُ+ثِير+ُ مُعْضِل+َة+ً فِي وَجْه+ِ ال+مُخَطِّط+ِيْنَ ال+أَمْرِيكِيّ+ِيْنَ \" .",
      "وَ- -تَمَكَّن+َ ال+سائِق+ُ مِن مُغادَر+َة+ِ ال+مَكان+ِ بَعْد+َ- -ما فَتَّش+َ ال+جُنُود+ُ سَيّار+َت+َ- -هُ .",
      "وَ- -يُ+طالِب+ُ ال+فِلَسْطِينِيّ+ُونَ بِ- -قُوّ+َة+ِ حِماي+َة+ٍ دُوَلِيّ+َة+ٍ ال+أَمْر+ُ الَّذِي تَ+رْفُض+ُ- -هُ إِسْرائِيل+ُ .",
      "ذَبْح+ُ ال+طُغا+َة+ِ لِ- -ال+عِباد+ِ",
      "وَ- -أَضاف+َ : \" عَلَى رَغْم+ِ كُلّ+ِ ال+ٱِتِّهام+ات+ِ ال+إِسْرائِيلِيّ+َة+ِ لِ- -حِزْب+ِ اللّٰه+ِ لَم تَ+جْرُؤ إِسْرائِيل+ُ عَلَى قَصْف+ِ هَدَف+ٍ فِي لُبْنان+َ لِ- -أَنَّ- -ها تَ+عْرِف+ُ أَنَّ ال+ٱِعْتِداء+َ عَلَي- -هِ ( . . . ) سَ- -يَ+كُون+ُ مُكَلَّف+اً جِدّ+اً لِ- -إِسْرائِيل+َ \" .",
      "درِسْدِن ( أَلْمانِيا ) 15-7 ( إِف ب ) -",
      "وَ- -أَحْرَز+َت ال+كُورِيّ+َة+ُ ال+جَنُوبِيّ+َة+ُ كو غي - هِيُون ال+مِيدالِي+َة+َ ال+ذَهَبِيّ+َة+َ فِي ال+تَزَحْلُق+ِ ال+سَرِيع+ِ عَلَى ال+جَلِيد+ِ فِي مِضْمار+ٍ قَصِير+ٍ لِ- -مَساف+َة+ِ 1500 مِتْر+ٍ , إِذ سَجَّل+َت 2.31.581 دَقِيق+َتَيْنِ مُتَقَدِّم+َة+ً مُواطِن+َت+َ- -ها تشوي أون - كيونغ , 2.31.610 وَ- -ال+بُلْغارِيّ+َة+َ يفغينيا رادانوفا .2 .31 .723 وَ- -قال+َت كو : \" لِ- -أَنَّ- -ها ال+أَلْعاب+ُ ال+أُولِمْبِيّ+َة+ُ , بَذَل+تُ جُهْد+اً أَكْبَر+َ مِن ذِي قَبْل+ٍ .",
      "وَ- -يَ+أْتِي+[نُلل] ال+إِعْلان+ُ عَن ال+مَشْرُوع+ِ غَدا+َة+َ إِعْلان+ِ أَرْبَع+ِ مَجْمُوع+ات+ٍ سَعُودِيّ+َة+ٍ إِنْشاء+َ شَرِك+َة+ٍ قابِض+َة+ٍ فِي دِمَشْق+َ بِ- -راسْمال+ِ 100 مِلْيُون+ِ دُولار+ٍ وَ- -تَ+هْدِف+ُ إِلَى تَمْوِيل+ِ مَشارِيع+َ إِنْمائِيّ+َة+ٍ فِي سُورِيا .",
      "فَ- -هَل ال+مَطْلُوب+ُ مِن- -ّا أَن نَ+مُوت+َ وَ- -نَحْنُ أَحْياء+ٌ ?",
      "وَ- -كَشَف+َت أَنَّ تِلْكَ ال+دُوَل+َ الَّتِي أَبْلَغ+َ عَن- -ها مُوسَى لِ- -ال+مَسْؤُول+ِينَ الَّذِينَ ٱِلْتَقا+[نُلل]- -هُم هِيَ : ال+سُودان+ُ , ال+صُومال+ُ وَ- -قَطَر+ُ .",
      "ال+سائِق+ُونَ الَّذِينَ ٱِتَّجَه+ُوا إِلَى تَبْدِيل+ِ مُحَرِّك+ات+ِ- -هِم ال+عامِل+َة+ِ عَلَى ال+مازُوت بِ- -أُخْرَى عامِل+َة+ٍ عَلَى ال+بِنْزِين+ِ واجَه+ُوا سُوق+اً منفلتة .",
      "2 - تُوماس السغارد ( نَرُوج ) 49.48.9",
      "فَ- -قَد ٱِسْتَوْعَب+َ مَجْلِس+ُ ال+أَمْن+ِ قَرار+َ ال+وِلاي+ات+ِ ال+مُتَّحِد+َة+ِ متفاديا ٱِجْتِهاد+اً قَد يُ+كَرِّس+ُ تَقْلِيد+اً فِي هٰذا ال+مَنْحَى .",
      "ال+قَوْل+ُ أَنَّ ال+عَصْر+َ الَّذِي تَ+حْكُم+ُ- -هُ ال+تَقْنِيّ+ات+ُ ال+حَدِيث+َة+ُ وَ- -تُ+حَقِّق+ُ فِي- -هِ لُغ+َة+ُ ال+كُومْبِيُوتِر ال+فُتُوح+ات+ِ ال+مُطَّرِد+َة+َ لَيْس+َ فِي- -هِ مَجال+ٌ لِ- -ال+شِعْر+ِ , بِ- -وَصْف+ِ- -هِ فاعِلِيّ+َة+ً إِبْداعِيّ+َة+ً مَحْض+َ مَجّانِيّ+َة+ٍ , تَ+نْضَوِي+[نُلل] عَلَى قَدْر+ٍ مِن ال+تَسَرُّع+ِ وَ- -ال+حَماس+َة+ِ ال+ٱِعْتِباطِيّ+َة+ِ لِ- -ٱِنْتِصار+ِ لُغ+َة+ِ ال+آل+َة+ِ وَ- -هِيَ لُغ+َة+ٌ مقوننة عَلَى لُغ+َة+ِ ال+إِبْداع+ِ الَّتِي تَ+قُوم+ُ فِي جَوْهَر+ِ- -ها عَلَى عُنْصُر+ِ ال+مُفاجَأ+َة+ِ وَ- -ال+إِتْيان+ِ بِ- -غَيْر+ِ ال+مُتَوَقَّع+ِ وَ- -ال+جَدِيد+ِ .",
      "وَ- -تُ+قام+ُ ال+سابِع+َة+َ صَباح+َ ال+يَوْم+ِ بِ- -تَوْقِيت+ِ بَيْرُوت+َ ال+تَجارِب+ُ ال+حُرّ+َة+ُ , وَ- -ال+سابِع+َة+َ صَباح+َ غَد+ٍ ال+سَبْت ال+تَجارِب+ُ ال+رَسْمِيّ+َة+ُ الَّتِي يَ+تَحَدَّد+ُ فِي ضَوْئ+ِ- -ها تَرْتِيب+ُ ٱِنْطِلاق+ِ ال+سائِق+ِينَ فِي ال+سِباق+ِ ال+ساع+َة+ِ ال+تاسِع+َة+ِ صَباح+َ ال+أَحَد+ِ .",
      "وَ- -ٱِنْتَقَد+ُوا بِ- -شِدّ+َة+ٍ لُجُوء+َ ال+سُلُط+ات+ِ إِلَى ٱِعْتِقال+ات+ٍ وَ- -ٱِسْتِدْعاء+ات+ٍ لِ- -ال+تَحْقِيق+ِ شَمِل+َت 64 إِسْلامِيّ+اً كُوَيْتِيّ+اً وَ- -عَشَر+ات+ٍ آخَر+ِيْنَ مِن ال+عَرَب+ِ , أُبْعِد+َ كَثِير+ُونَ مِن- -هُم مِن ال+بِلاد+ِ مِن دُون+ِ تُهْم+َة+ٍ , وَ- -هِيَ إِجْراء+ات+ٌ تَمّ+َت فِي ظِلّ+ِ \" ال+هِسْتِيرِيا ال+دَوْلِيّ+َة+ِ ضِدّ+َ كُلّ+ِ ما هُوَ إِسْلامِيّ+ٍ \" , كَ- -ما قال+َ نائِب+ٌ إِسْلامِيّ+ٌ لِ \" ال+حَيا+َة+ِ \" .",
      "تشِينِي ٱِلْتَقَى+[نُلل] عَلِيّ صالِح وَ- -ال+سُلْطان+َ قابوس",
      "نَدَّد+َ \" ٱِتِّحاد+ُ ال+رابِط+ات+ِ ال+لُبْنانِيّ+َة+ِ ال+مَسِيحِيّ+َة+ِ \" بِ- -ال+ٱِنْفِجار+ِ الَّذِي حَصَل+َ فِي طَرابُلُس وَ- -ٱِسْتَهْدَف+َ مَطْعَم+َ \" كينتاكي فرايد تشيكن \" , وَ- -طالَب+َ بِ- -مُلاحَق+َة+ِ ال+فاعِل+ِينَ وَ- -إِنْزال+ِ ال+قِصاص+ِ بِ- -هِم .",
      "وَ- -سَ- -يُ+رافِق+ُ باوِل وَفْد+ٌ إِعْلامِيّ+ٌ أَمِيرْكِيّ+ٌ .",
      "لٰكِنَّ عَمَلِيّ+َة+َ ال+شِراء+ِ ما زال+َت تَ+حْتاج+ُ إِلَى تَخَطِّي عَقَب+َة+ِ ال+سُلُط+ات+ِ ال+مَسْؤُول+َة+ِ عَن قِطاع+ِ ال+مُنافَس+َة+ِ .",
      "أَوْدَع+َ بَيْن+َ ضُلُوع+ِ مَقاعِد+ِ- -ها سِرّ+َ- -هُ وَ- -مَضَى+[نُلل] إِلَي- -ها , وَ- -حِين+َ سَأَل+تُ- -هُ عَن سَبَب+ِ تَأَخُّر+ِ- -هِ تَخَلَّى+[نُلل] عَن وَقار+ِ- -هِ وَ- -طَمْأَن+َ- -ها بِ- -ٱِبْتِسام+َة+ٍ فِي- -ها ال+رِضَى وَ- -صَفاء+ُ ال+حُبُور+ِ .",
      "وَ- -حَسَب+َ ال+نَتائِج+ِ ال+تَمْهِيدِيّ+َة+ِ فَ- -إِنَّ ال+جُمْهُورِيّ+َ جُورْج بُوش يَ+تَقَدَّم+ُ بِ- -مُعَدَّل+ِ 300 صَوْت+ٍ فِي دَوائِر+ِ فلُورِيدا ال 67 .",
      "1948 - ال+زَعِيم+ُ ال+وَطَنِيّ+ُ ال+مِصْرِيّ+ُ سَعْد زَغْلُول عيود مِن مَنْفا- -هُ .",
      "إِنَّ- -نا هادِئ+ُونَ .",
      "وَ- -أَنْه+َت ماكْماهُون ال+سِباق+َ الَّذِي يَ+تَضَمَّن+ُ مُنافَس+ات+ٍ فِي ثَلاث+ِ لَعَب+ات+ٍ هِيَ ( سِباح+َة+ُ 1500 م ) وَ- -دَرّاج+ات+ٌ هَوائِيّ+َة+ٌ ( 40 كلم ) وَ 10 كلم عَدْو+اً فِي زَمَن+ٍ مِقْدار+ُ- -هُ 40 ر 00 ر 2 ساع+َة+ً بِ- -فارِق+ِ ثانِي+َتَيْنِ فَقَط عَن صاحِب+َة+ِ ال+فِضِّيّ+َة+ِ ال+أُسْتُرالِيّ+َة+ِ مِيشِيلِي جُونْز ال+مُصَنَّف+َة+ِ أُولَى عالَمِيّ+اً وَ- -الَّتِي كان+َت مُرَشَّح+َة+ً بِ- -قُوّ+َة+ٍ كَي تَ+مْنَح+َ أُسْتُرالِيا ال+دَوْل+َة+َ ال+مُضِيف+َة+َ مِيدالِي+َت+َ- -ها ال+ذَهَبِيّ+َة+َ ال+أُولَى فِي هٰذِهِ ال+أَلْعاب+ِ .",
      "خَبَر+ُ تَدْمِير+ِ فُنْدُق+ِ هِيلْتُون وَ- -ٱِسْتِبْدال+ِ- -هِ مُسْتَقْبَل+اً بِ- -فُنْدُق+ٍ جَدِيد+ٍ كان+َ يُ+مْكِن+ُ أَن يَ+كُون+َ خَبَر+اً عادِي+اً فِي ال+صَفَح+ات+ِ ال+ٱِقْتِصادِيّ+َة+ِ لِ- -ال+صُحُف+ِ ال+يَوْمِيّ+َة+ِ الَّتِي تَ+نْشُر+ُ أَخْبار+َ ال+ٱِقْتِصاد+ِ وَ- -ال+ٱِسْتِثْمار+ِ فِي لُبْنان+َ , وَ- -لٰكِن كَوْن+ُ- -هُ فُنْدُق+ُ هِيلْتُون أَيْ ال+قائِم+َة+ُ ال+رابِع+َة+ُ لِ- -ساح+َة+ِ مَعْرَك+َة+ٍ ضارِي+َة+ٍ نَشِب+َت عام+َيْ 1975 وَ 1976 فِي أَثْناء+ِ ال+حَرْب+ِ ال+لُبْنانِيّ+َة+ِ مَع+َ السان جُورْج وَ- -ال+فِينِيسِيا وَ- -ال+هوليداي إِن , هٰذا ال+فُنْدُق+ُ الَّذِي ٱُسْتُشْهِد+َ داخِل+َ- -هُ وَ- -عَلَى أَطْراف+ِ- -هِ وَ- -ال+شَوارِع+ِ الَّتِي تَ+حُوط+ُ- -هُ مِئ+ات+ٌ مِن ال+شَباب+ِ ال+لُبْنانِيّ+ِينَ , فَ- -إِنَّ خَبَر+َ تَدْمِير+ِ- -هِ لَن يَ+مُرّ+َ دُون+َ ال+تَوَقُّف+ِ عِنْد+َ- -هُ لِ- -أَخْذ+ِ ال+عِبْر+َة+ِ .",
      "إِلّا وَ- -تُ+طالِع+ُ- -نا ال+صُحُف+ُ ال+عَرَبِيّ+َة+ُ بِ- -أَكْثَر+ِ مِن خَبَر+ٍ أَو مَقال+َة+ٍ مَوْضُوع+ُ- -ها صُور+َة+ُ ال+عَرَب+ِ وَ- -ال+مُسْلِم+ِينَ وَ- -ال+تَشْوِيه+ُ ال+لاحِق+ُ بِ- -هِما فِي ال+غَرْب+ِ .",
      "وَ- -أَضاف+َت \" مِيس \" إِنَّ \" ال+مُنَظَّم+َة+َ حَذَّر+َت مِن أَنَّ ال+شَرِك+ات+ِ ال+نَفْطِيّ+َة+َ الَّتِي تَ+رْفُض+ُ ال+قِيام+َ بِ- -ذٰلِكَ سَ- -تَ+خْسَر+ُ ال+تَسْلِيم+َ ال+مُخَصَّص+َ لَ- -ها \" .",
      "وَ- -ظَهَر+َ إِثْر+َ ذٰلِكَ أَنَّ ال+صَوارِيخ+َ مُوَجَّه+َة+ٌ إِلَى ال+يَمَن+ِ .",
      "أَمّا ال+مَسِيحِيّ+ُونَ الَّذِينَ لَم يَ+فُوز+ُوا بِ- -أَيّ+ِ مَقْعَد+ٍ نِيابِيّ+ٍ فِي ال+ٱِنْتِخاب+ات+ِ ال+تَشْرِيعِيّ+َة+ِ ال+سابِق+َة+ِ فَ- -حَصَل+ُوا ال+آن+َ عَلَى ثَلاث+َة+ِ مَقاعِد+َ كان+َت مِن نَصِيب+ِ وَزِير+ِ ال+ٱِقْتِصاد+ِ يُوسِف بُطْرُس غالِي ( قِبْطِيّ+ٌ ) وَ- -رَجُل+ِ ال+أَعْمال+ِ مُنِير فَخْر عَبْد ال+نُور مِن حِزْب+ِ ال+وَفْد+ِ إِضاف+َة+ً إِلَى رَجُل+ِ ال+أَعْمال+ِ ال+كاثُولِيكِيّ+ِ رامِي لكح .",
      "أَكَّد+َ ال+عاهِل+ُ ال+سَعُودِيّ+ُ ال+مَلِك+ُ فَهْد بِن عَبْد ال+عَزِيز فِي رِسال+َة+ٍ بَعَث+َ بِ- -ها إِلَى ال+رَئِيس+ِ ال+أَمِيرْكِيّ+ِ جُورْج بُوش فِي ال+ذِكْرَى ال+أُولَى لِ- -هَجَم+ات+ِ 11 أَيْلُول+َ , ٱِلْتِزام+َ بِلاد+ِ- -هِ حَمْل+َة+َ مُكافَح+َة+ِ ال+إِرْهاب+ِ , مُشَدِّد+اً فِي ال+وَقْت+ِ ذات+ِ- -هِ عَلَى \" رُسُوخ+ِ \" ال+عَلاق+ات+ِ ال+ثُنائِيّ+َة+ِ .",
  };

  private final String[] tokReferences = {
      "و كان من المقرر ان يشارك في الدورة ايضا فريقا الرياضي اللبناني و الخليج السعودي لكن هما اعتذرا في وقت متاخر من مساء اليوم الثلاثاء .",
      "تستعد الولايات المتحدة ل حرب دولية ضد الارهاب س تنفق علي ها 40 بليون دولار .",
      "غوريل ينتقد خطة عنان ل توحيد الجزيرة",
      "و رد شاهرودي : \" يمثل لبنان الخندق الاول الذي يواجه العدوان الصهيوني و يختزن شعبا طيبا و خيرا ( . . . ) \" .",
      "و قد عثر على جثت ه اسفل جسر على الطريق السريع في بلدة فوسانو , و كانت سيارت ه متوقفة على الجسر الذي يبلغ علو ه ثمانين مترا , و اشارت الشرطة الى عدم عثور ها على اي اشارة ب وجود سرقة او اعتداء .",
      "و كان الوباء نفس ه اودى ب حياة 300 شخص في الكونغو عام 1995 و حوالي خمسين اخرين في الغابون .",
      "و لم تنس المؤلفة كلودين ربط حياة ال تشابلن في الحوادث التاريخية التي رافقت هم في \" عاشقا الزمن المعاصر \" .",
      "اعلن رئيس اندونيسيا عبد الرحمن وحيد اليوم الثلاثاء ان ه التقى وزير التعاون الاقليمي الاسرائيلي شيمون بيريز امس , على ان يلتقي الرئيس الفلسطيني ياسر عرفات مساء اليوم .",
      "و بعد ثمانية ايام من الانتخابات الرئاسية الاكثر احتداما في تاريخ الولايات المتحدة لا يزال انتخاب رئيس جديد مرهونا اليوم الاربعاء ب احتمال مواصلة عمليات الفرز اليدوية في بعض مقاطعات ولاية فلوريدا حيث تفيد النتيجة الاولية ب ان جورج بوش يتقدم على منافس ه ال غور ب 300 صوت .",
      "و ماذا على لبنان , قطاعا عاما و صناعيا منتجا ان يفعل ل مواجهة مازق عدم القدرة من الافادة , من التحولات الحاصلة ب ما يضمن استقرار اقتصادات ها الوطنية ? \" .",
      "منح رئيس الجمهورية اميل لحود الرئيس السابق ل مجلس القضاء الاعلى نصري لحود امس في القصر الجمهوري وسام الارز الوطني من رتبة \" ضابط اكبر \" , ل مناسبة انتهاء ولايت ه في رئاسة مجلس القضاء .",
      "و قد ادرج مسابقة الترياتلون رسميا في الالعاب الاولمبية ل المرة الاولى .",
      "و مذ ذاك , احاول ان اعمل ب هدي والد ي , ذاكرا نصيحت ه على الدوام , ف الشيء الجميل النبيل يجب ان يعمم على الناس , لا ان يبقى طي الصدور و القلوب .",
      "و ادانة الحزب الشيوعي ل الارهاب , لم تنس الاشارة الى ان \" العمل الارهابي ثمرة مرة ل السياسة الامريكية نفس ها الداخلية و الخارجية و ل نهج الاحتكارات الراسمالية و دول ها \" , متوقعة \" س تبقى الولايات المتحدة معرضة ل الارهاب \" .",
      "و قال : \" لا بد من الانتقال ب الاعلام العربي من الساحة الضيقة الى الساحة الدولية ل ان المرحلة تتطلب وضع خطة عربية واحدة \" .",
      "و هذه المداخلة الاولى ل رئيس الاركان الاسرائيلي امام هذا المنتدى الذي يضم ضباط الاركان و الضباط العاملين , منذ تسلم ه مسؤوليات ه في 9 تموز خلفا ل الجنرال شاؤول موفاز .",
      "و تعتبر المباراة قمة المجموعة الثانية لكن الافضلية بعد اداء المنتخبين الجمعة الماضي تصب في مصلحة الكوريين , من حيث الجهوزية و السرعة و استثمار الفرص , لكن في كرة القدم ل كل مباراة ظروف ها و حسابات ها الخاصة , و ب التالي يصعب التكهن ب ما س يحصل غدا ل ان العملاقين الكويتي و الكوري يملكان جميع مقومات الفوز .",
      "و الواقع في الادارة الاميركية اليوم يثبت ان اكثر شخصية ل ها نفوذ على بوش هو ب حسب الصحف الاميركية , ديك تشيني .",
      "ب \" قبر كوبران \" ل رافيل افتتح لقاء ه ب الجمهور اللبناني , و هي توليفة مركبة من ست مقطوعات كرم ب ها رافيل ذكرى كوبران الموسيقي في القرن السابع عشر و عصر الباروك الذي عاش في ه .",
      "و هذا يثير معضلة في وجه المخططين الامريكيين \" .",
      "و تمكن السائق من مغادرة المكان بعد ما فتش الجنود سيارت ه .",
      "و يطالب الفلسطينيون ب قوة حماية دولية الامر الذي ترفض ه اسرائيل .",
      "ذبح الطغاة ل العباد",
      "و اضاف : \" على رغم كل الاتهامات الاسرائيلية ل حزب الله لم تجرؤ اسرائيل على قصف هدف في لبنان ل ان ها تعرف ان الاعتداء علي ه ( . . . ) س يكون مكلفا جدا ل اسرائيل \" .",
      "درسدن ( المانيا ) 15-7 ( اف ب ) -",
      "و احرزت الكورية الجنوبية كو غي - هيون الميدالية الذهبية في التزحلق السريع على الجليد في مضمار قصير ل مسافة 1500 متر , اذ سجلت 2.31.581 دقيقتين متقدمة مواطنت ها تشوي اون - كيونغ , 2.31.610 و البلغارية يفغينيا رادانوفا . 2 . 31 . 723 و قالت كو : \" ل ان ها الالعاب الاولمبية , بذلت جهدا اكبر من ذي قبل .",
      "و ياتي الاعلان عن المشروع غداة اعلان اربع مجموعات سعودية انشاء شركة قابضة في دمشق ب راسمال 100 مليون دولار و تهدف الى تمويل مشاريع انمائية في سوريا .",
      "ف هل المطلوب من ا ان نموت و نحن احياء ?",
      "و كشفت ان تلك الدول التي ابلغ عن ها موسى ل المسؤولين الذين التقا هم هي : السودان , الصومال و قطر .",
      "السائقون الذين اتجهوا الى تبديل محركات هم العاملة على المازوت ب اخرى عاملة على البنزين واجهوا سوقا منفلتة .",
      "2 - توماس السغارد ( نروج ) 49.48.9",
      "ف قد استوعب مجلس الامن قرار الولايات المتحدة متفاديا اجتهادا قد يكرس تقليدا في هذا المنحى .",
      "القول ان العصر الذي تحكم ه التقنيات الحديثة و تحقق في ه لغة الكومبيوتر الفتوحات المطردة ليس في ه مجال ل الشعر , ب وصف ه فاعلية ابداعية محض مجانية , تنضوي على قدر من التسرع و الحماسة الاعتباطية ل انتصار لغة الالة و هي لغة مقوننة على لغة الابداع التي تقوم في جوهر ها على عنصر المفاجاة و الاتيان ب غير المتوقع و الجديد .",
      "و تقام السابعة صباح اليوم ب توقيت بيروت التجارب الحرة , و السابعة صباح غد السبت التجارب الرسمية التي يتحدد في ضوئ ها ترتيب انطلاق السائقين في السباق الساعة التاسعة صباح الاحد .",
      "و انتقدوا ب شدة لجوء السلطات الى اعتقالات و استدعاءات ل التحقيق شملت 64 اسلاميا كويتيا و عشرات اخرين من العرب , ابعد كثيرون من هم من البلاد من دون تهمة , و هي اجراءات تمت في ظل \" الهستيريا الدولية ضد كل ما هو اسلامي \" , ك ما قال نائب اسلامي ل \" الحياة \" .",
      "تشيني التقى علي صالح و السلطان قابوس",
      "ندد \" اتحاد الرابطات اللبنانية المسيحية \" ب الانفجار الذي حصل في طرابلس و استهدف مطعم \" كينتاكي فرايد تشيكن \" , و طالب ب ملاحقة الفاعلين و انزال القصاص ب هم .",
      "و س يرافق باول وفد اعلامي اميركي .",
      "لكن عملية الشراء ما زالت تحتاج الى تخطي عقبة السلطات المسؤولة عن قطاع المنافسة .",
      "اودع بين ضلوع مقاعد ها سر ه و مضى الي ها , و حين سالت ه عن سبب تاخر ه تخلى عن وقار ه و طمان ها ب ابتسامة في ها الرضى و صفاء الحبور .",
      "و حسب النتائج التمهيدية ف ان الجمهوري جورج بوش يتقدم ب معدل 300 صوت في دوائر فلوريدا ال 67 .",
      "1948 - الزعيم الوطني المصري سعد زغلول عيود من منفا ه .",
      "ان نا هادئون .",
      "و انهت ماكماهون السباق الذي يتضمن منافسات في ثلاث لعبات هي ( سباحة 1500 م ) و دراجات هوائية ( 40 كلم ) و 10 كلم عدوا في زمن مقدار ه 40 ر 00 ر 2 ساعة ب فارق ثانيتين فقط عن صاحبة الفضية الاسترالية ميشيلي جونز المصنفة اولى عالميا و التي كانت مرشحة ب قوة كي تمنح استراليا الدولة المضيفة ميداليت ها الذهبية الاولى في هذه الالعاب .",
      "خبر تدمير فندق هيلتون و استبدال ه مستقبلا ب فندق جديد كان يمكن ان يكون خبرا عاديا في الصفحات الاقتصادية ل الصحف اليومية التي تنشر اخبار الاقتصاد و الاستثمار في لبنان , و لكن كون ه فندق هيلتون اي القائمة الرابعة ل ساحة معركة ضارية نشبت عامي 1975 و 1976 في اثناء الحرب اللبنانية مع السان جورج و الفينيسيا و الهوليداي ان , هذا الفندق الذي استشهد داخل ه و على اطراف ه و الشوارع التي تحوط ه مئات من الشباب اللبنانيين , ف ان خبر تدمير ه لن يمر دون التوقف عند ه ل اخذ العبرة .",
      "الا و تطالع نا الصحف العربية ب اكثر من خبر او مقالة موضوع ها صورة العرب و المسلمين و التشويه اللاحق ب هما في الغرب .",
      "و اضافت \" ميس \" ان \" المنظمة حذرت من ان الشركات النفطية التي ترفض القيام ب ذلك س تخسر التسليم المخصص ل ها \" .",
      "و ظهر اثر ذلك ان الصواريخ موجهة الى اليمن .",
      "اما المسيحيون الذين لم يفوزوا ب اي مقعد نيابي في الانتخابات التشريعية السابقة ف حصلوا الان على ثلاثة مقاعد كانت من نصيب وزير الاقتصاد يوسف بطرس غالي ( قبطي ) و رجل الاعمال منير فخر عبد النور من حزب الوفد اضافة الى رجل الاعمال الكاثوليكي رامي لكح .",
      "اكد العاهل السعودي الملك فهد بن عبد العزيز في رسالة بعث ب ها الى الرئيس الاميركي جورج بوش في الذكرى الاولى ل هجمات 11 ايلول , التزام بلاد ه حملة مكافحة الارهاب , مشددا في الوقت ذات ه على \" رسوخ \" العلاقات الثنائية .",
  };

  public void testArabicTokenizer() {
    assert(untokInputs.length == tokReferences.length);

    TokenizerFactory<CoreLabel> tf = ArabicTokenizer.atbFactory();
    tf.setOptions("removeProMarker");
    tf.setOptions("removeSegMarker");
    tf.setOptions("removeMorphMarker");

    for (int i = 0; i < untokInputs.length; ++i) {
      String line = untokInputs[i];
      Tokenizer<CoreLabel> tokenizer = tf.getTokenizer(new StringReader(line));
      List<CoreLabel> tokens = tokenizer.tokenize();
      String tokenizedLine = SentenceUtils.listToString(tokens);
      String reference = tokReferences[i];
      assertEquals("Tokenization deviates from reference", reference, tokenizedLine);
    }
  }

  public void testCharOffsets() {
    String untokInput = "إِنَّ- -نا هادِئ+ُونَ .";
    int[] beginOffsets = {0, 7, 11, 22};
    int[] endOffsets = {6, 10, 21, 23};
    
    TokenizerFactory<CoreLabel> tf = ArabicTokenizer.atbFactory();
    tf.setOptions("removeProMarker");
    tf.setOptions("removeSegMarker");
    tf.setOptions("removeMorphMarker");
    
    Tokenizer<CoreLabel> tokenizer = tf.getTokenizer(new StringReader(untokInput));
    List<CoreLabel> tokens = tokenizer.tokenize();
    assertEquals("Number of tokens doesn't match reference", tokens.size(), beginOffsets.length);
    for (int i = 0; i < beginOffsets.length; i++) {
      assertEquals("Char begin offset deviates from reference", beginOffsets[i], tokens.get(i).beginPosition());
      assertEquals("Char end offset deviates from reference", endOffsets[i], tokens.get(i).endPosition());
    }
  }
}
