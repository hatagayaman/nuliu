package com.example.demo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.MailSender;
//import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.data.GBConstants;
import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.MovieDto;
import com.example.demo.dto.TagDto;
import com.example.demo.form.ContactForm;
import com.example.demo.service.MovieService;
import com.example.demo.service.TagService;

@Controller
@RequestMapping("/")
public class TopController {
	@Autowired
	private MovieService movieService;

	@Autowired
	private TagService tagService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String top(Model model) {
		// 注目動画
		MovieDto attention = new MovieDto();
		model.addAttribute("attention", attention);

		// top動画一覧
		Map<String, List<MovieDto>> movieMap = new LinkedHashMap<String, List<MovieDto>>();
		movieMap.put("新着動画", movieService.getMovieListLimit(10));
		movieMap.put("人気動画", movieService.getPopularMovieListLimit(10));
		movieMap.put("美乳・巨乳", movieService.getMovieListByCategoryLimit(1,10));
		movieMap.put("人妻・熟女", movieService.getMovieListByCategoryLimit(2,10));
		movieMap.put("企画", movieService.getMovieListByCategoryLimit(3,10));
		movieMap.put("素人", movieService.getMovieListByCategoryLimit(4,10));
		movieMap.put("OL・お姉さん", movieService.getMovieListByCategoryLimit(5,10));
		movieMap.put("ハメ撮り", movieService.getMovieListByCategoryLimit(6,10));
		movieMap.put("オナニー", movieService.getMovieListByCategoryLimit(7,10));
		movieMap.put("制服・コスプレ", movieService.getMovieListByCategoryLimit(8,10));
		movieMap.put("野外・露出", movieService.getMovieListByCategoryLimit(9,10));
		movieMap.put("盗撮", movieService.getMovieListByCategoryLimit(10,10));
		movieMap.put("レズ", movieService.getMovieListByCategoryLimit(11,10));
		movieMap.put("ゲイ", movieService.getMovieListByCategoryLimit(12,10));
		//movieMap.put("海外", movieService.getMovieListByCategoryLimit(13,10));
		movieMap.put("アニメ", movieService.getMovieListByCategoryLimit(14,10));
		model.addAttribute("movieMap", movieMap);

		// カテゴリ一覧
		List<CategoryDto> categoryList = movieService.getCategoryList();
		model.addAttribute("categoryList", categoryList);

		// タグ
		List<TagDto> tagList = tagService.getTagList(30);
		model.addAttribute("tagList", tagList);

		// フッター領域のタグ
		List<TagDto> footerTagList = tagService.getTagList(15);
		model.addAttribute("footerTagList", footerTagList);

		return "top";
	}

	// 新着動画
	@RequestMapping(value = "/searchNew", method = RequestMethod.GET)
	public String newmovie(Model model, @RequestParam(name="page", defaultValue = "1") int page) {
		// 動画一覧
		List<MovieDto> movieList = movieService.getMovieList(page);
		long allRecordCnt = movieService.getMovieRecordCount();
		model.addAttribute("resultTitle", "新着動画：" + allRecordCnt + "件");
		model.addAttribute("movieList", movieList);
		// カテゴリ一覧
		List<CategoryDto> categoryList = movieService.getCategoryList();
		model.addAttribute("categoryList", categoryList);
		// タグ
		List<TagDto> tagList = tagService.getTagList(30);
		model.addAttribute("tagList", tagList);
		// ページング
		int totalPages = (int) Math.ceil(allRecordCnt/GBConstants.NUM_PER_PAGE);
		if (allRecordCnt % GBConstants.NUM_PER_PAGE != 0) totalPages += 1;
		List<Integer> pageNoList = new LinkedList<>();
		for (int i = 1; i <= totalPages; i++) {
			if (page != i) pageNoList.add(i);
		}
		model.addAttribute("pageFlag", totalPages != 0);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageNoList", pageNoList);
		model.addAttribute("prePageFlag", (page != 1));
		model.addAttribute("prePage", "/searchNew?page=" + ((page == 1) ? 1: page - 1));
		model.addAttribute("nextPageFlag", (page != totalPages));
		model.addAttribute("nextPage", "/searchNew?page=" + (page + 1));

		// フッター領域のタグ
		List<TagDto> footerTagList = tagService.getTagList(15);
		model.addAttribute("footerTagList", footerTagList);
		return "searchResult";
	}

	// 動画閲覧ページ遷移
	@RequestMapping(value = "/movieroom/{movieid}", method = RequestMethod.GET)
	public String movieroom(Model model, @PathVariable int movieid) {
		MovieDto dto = movieService.getMovieDetailList(movieid);
		model.addAttribute("movie", dto);
		movieService.countMovieViews(movieid);
		// タグ
		model.addAttribute("tagListById", tagService.getTagListByMovieId(movieid, true));
		// 関連動画
		List<MovieDto> relatedList = movieService.getRelatedMovieList(movieid);
		model.addAttribute("relatedList", relatedList);

		// 新着動画
		List<MovieDto> movieList = movieService.getMovieList(1);
		List<MovieDto> newMovieList = new LinkedList<>();
		if (newMovieList.size() > 10) {
			for (int i = 0; i < 10; i++) {
				newMovieList.add(movieList.get(i));
			}
		}
		model.addAttribute("newMovieList", newMovieList);

		// カテゴリ一覧
		List<CategoryDto> categoryList = movieService.getCategoryList();
		model.addAttribute("categoryList", categoryList);
		// その他タグ一覧
		model.addAttribute("tagList", tagService.getTagList(30));
		// フッター領域のタグ
		List<TagDto> footerTagList = tagService.getTagList(15);
		model.addAttribute("footerTagList", footerTagList);
		return "movieroom";
	}

	// 人気動画検索
	@RequestMapping(value = "/searchPopular", method = RequestMethod.GET)
	public String searchPopular(Model model, @RequestParam(name="page", defaultValue = "1") int page) {
		List<MovieDto> movieList = movieService.getPopularMovieList(page, true);
		int allrecords = movieService.getPopularMovieList(page, false).size();
		model.addAttribute("resultTitle", "人気動画");
		model.addAttribute("movieList", movieList);

		model.addAttribute("categoryList", movieService.getCategoryList());
		model.addAttribute("tagList", tagService.getTagList(30));

		// ページング
		int totalPages = (int) Math.ceil(allrecords / GBConstants.NUM_PER_PAGE);
		if (allrecords % GBConstants.NUM_PER_PAGE != 0) totalPages += 1;
		List<Integer> pageNoList = new LinkedList<>();
		for (int i = 1; i <= totalPages; i++) {
			if (page != i) pageNoList.add(i);
		}
		model.addAttribute("pageFlag", totalPages != 0);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageNoList", pageNoList);
		model.addAttribute("prePageFlag", (page != 1));
		model.addAttribute("prePage", "/searchPopular?page=" + ((page == 1) ? 1: page - 1));
		model.addAttribute("nextPageFlag", (page != totalPages));
		model.addAttribute("nextPage", "/searchPopular?page=" + (page + 1));
		// フッター領域のタグ
		List<TagDto> footerTagList = tagService.getTagList(15);
		model.addAttribute("footerTagList", footerTagList);
		return "searchResult";
	}

	// フリーワード検索
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(Model model, @RequestParam("word") String word, @RequestParam(name="page", defaultValue = "1") int page) {
		List<MovieDto> movieList = new ArrayList<>();
		int allrecords = 0;
		if (!StringUtils.isEmpty(word)) {
			movieList = movieService.getMovieListByWord(word, page, true);
			allrecords = movieService.getMovieListByWord(word, page, false).size();
		}

		model.addAttribute("resultTitle", "「" + word + "」で検索：" + allrecords + "件");
		model.addAttribute("movieList", movieList);
		List<CategoryDto> categoryList = movieService.getCategoryList();
		model.addAttribute("categoryList", categoryList);
		// タグ
		List<TagDto> tagList = tagService.getTagList(30);
		model.addAttribute("tagList", tagList);

		// ページング
		int totalPages = (int) Math.ceil(allrecords / GBConstants.NUM_PER_PAGE);
		if (allrecords % GBConstants.NUM_PER_PAGE != 0 && totalPages != 0) totalPages += 1;
		List<Integer> pageNoList = new LinkedList<>();
		for (int i = 1; i <= totalPages; i++) {
			if (page != i) pageNoList.add(i);
		}
		model.addAttribute("pageFlag", totalPages != 0);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageNoList", pageNoList);
		model.addAttribute("prePageFlag", (page != 1));
		model.addAttribute("prePage", "/search?word=" + word + "&?page=" + ((page == 1) ? 1: page - 1));
		model.addAttribute("nextPageFlag", (page != totalPages));
		model.addAttribute("nextPage", "/search?word=" + word + "&?page=" + (page + 1));
		// フッター領域のタグ
		List<TagDto> footerTagList = tagService.getTagList(15);
		model.addAttribute("footerTagList", footerTagList);
		return "searchResult";
	}

	// カテゴリ検索
	@RequestMapping(value = "/searchCategory/{cateid}", method = RequestMethod.GET)
	public String searchCategory(Model model, @PathVariable int cateid, @RequestParam(name="page", defaultValue = "1") int page) {
		List<MovieDto> movieList = movieService.getMovieListByCategory(cateid, page, true);
		int allRecords = movieService.getMovieListByCategory(cateid, page, false).size();
		model.addAttribute("resultTitle",
				"「" + movieList.get(0).getCategoryName() + "」カテゴリの動画：" + allRecords + "件");

		model.addAttribute("movieList", movieList);

		List<CategoryDto> categoryList = movieService.getCategoryList();
		model.addAttribute("categoryList", categoryList);
		// タグ
		List<TagDto> tagList = tagService.getTagList(30);
		model.addAttribute("tagList", tagList);

		// ページング
		int totalPages = (int) Math.ceil(allRecords / GBConstants.NUM_PER_PAGE);
		if (allRecords % GBConstants.NUM_PER_PAGE != 0) totalPages += 1;
		List<Integer> pageNoList = new LinkedList<>();
		for (int i = 1; i <= totalPages; i++) {
			if (page != i) pageNoList.add(i);
		}
		model.addAttribute("pageFlag", totalPages != 0);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageNoList", pageNoList);
		model.addAttribute("prePageFlag", (page != 1));
		model.addAttribute("prePage", "/searchCategory/" + cateid + "?page=" + ((page == 1) ? 1: page - 1));
		model.addAttribute("nextPageFlag", (page != totalPages));
		model.addAttribute("nextPage", "/searchCategory/" + cateid + "?page=" + (page + 1));

		// フッター領域のタグ
		List<TagDto> footerTagList = tagService.getTagList(15);
		model.addAttribute("footerTagList", footerTagList);
		return "searchResult";
	}

	@RequestMapping("/adv")
	public String adv() {
		return "adv";
	}

	@RequestMapping("/mypage")
	public String mypage(Model model, Principal principal) {
		Authentication authentication = (Authentication) principal;
		UserDetails user = (UserDetails) authentication.getPrincipal();
		String url = "/";
		if (user != null) {
			if (user.getAuthorities().toString().contains("ADMIN")) {
				url = "/admin";
			} else if (user.getAuthorities().toString().contains("CAST")) {
				url = "/cast";
			} else if (user.getAuthorities().toString().contains("MEMBER")) {
				url = "/member";
			}
		}
		return "redirect:" + url;
	}

	@RequestMapping("/contact")
	public String contact(Model model) {
		model.addAttribute("contactForm", new ContactForm());
		// カテゴリ一覧
		List<CategoryDto> categoryList = movieService.getCategoryList();
		model.addAttribute("categoryList", categoryList);
		// フッター領域のタグ
		List<TagDto> footerTagList = tagService.getTagList(15);
		model.addAttribute("footerTagList", footerTagList);
		return "contact";
	}

//	// お問い合わせページ遷移
//	@Autowired
//    private MailSender sender;
//
//	private static final String KAIGYO = System.getProperty("line.separator");
//
//	@RequestMapping("/contact/send")
//	public String send(@Valid @ModelAttribute ContactForm contactForm, BindingResult result, Model model) {
//		if (result.hasErrors()) {
//            for(FieldError err: result.getFieldErrors()) {
////                log.debug("error code = [" + err.getCode() + "]");
//            	System.out.println("error code = [" + err.getCode() + "]");
//            }
//    		List<CategoryDto> categoryList = movieService.getCategoryList();
//    		model.addAttribute("categoryList", categoryList);
//            return "contact";
//        }
//		SimpleMailMessage msg = new SimpleMailMessage();
//		msg.setTo("XXXXXXXXXXX@gmail.com");
//		msg.setSubject("GBNETからメールがありました");
//		msg.setText(
//				"name:" +
//				contactForm.getName() +
//				KAIGYO +
//				"email:" +
//				contactForm.getEmail() +
//				KAIGYO +
//				"からメールです。"+
//				KAIGYO +
//				"件名：" +
//				KAIGYO +
//				contactForm.getSubject() +
//				KAIGYO +
//				"内容：" +
//				KAIGYO +
//				contactForm.getBody());
//        this.sender.send(msg);
//		return "sendcomplete";
//	}
}
