package com.ex.newWeb.Controller;

import com.ex.newWeb.Dto.PlayListDto;
import com.ex.newWeb.models.PlayList;
import com.ex.newWeb.models.UserEntity;
import com.ex.newWeb.security.SecurityUtil;
import com.ex.newWeb.service.PlayListService;
import com.ex.newWeb.service.UserService;
import com.ex.newWeb.service.impl.PlayListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class PlayListController {
    private PlayListService playListService;
    private UserService userService;


    @Autowired
    public PlayListController(PlayListService playListService, UserService userService) {
        this.playListService = playListService;
        this.userService = userService;
    }


    @GetMapping("/playLists")
    public String listPlayList(Model model){
        UserEntity user = new UserEntity();
        List<PlayListDto> playLists = playListService.findAllPlayLists();
        String username = SecurityUtil.getSessionUser();
        if(username != null){
            user = userService.findByUsername(username);
            model.addAttribute("user",user);
        }
        model.addAttribute("user",user);
        model.addAttribute("playLists", playLists);
        return "playList-list";
    }

    @GetMapping("/playLists/{playListId}")
    public String playListDetail(@PathVariable("playListId") Long playListId, Model model){
        UserEntity user = new UserEntity();
        PlayListDto playListDto = playListService.findPlayListById(playListId);
        String username = SecurityUtil.getSessionUser();
        if(username != null){
            user = userService.findByUsername(username);
            model.addAttribute("user",user);
        }
        model.addAttribute("user",user);
        model.addAttribute("playList",playListDto);
        return "playList-detail";
    }
    @GetMapping("/playLists/search")
    public String seachPlayList(@RequestParam(value = "query") String query, Model model){
        List<PlayListDto> playLists = playListService.searchClubs(query);
        model.addAttribute("playLists", playLists);
        return "playList-list";
    }

    @GetMapping("/playLists/new")
    public String createPlayList(Model model){
        PlayList playList = new PlayList();
        model.addAttribute("playList", playList);
        return "playList-create";
    }

    @GetMapping("/playLists/{playListId}/delete")
    public String playListDelete(@PathVariable("playListId") Long playListId, Model model){
        playListService.delete(playListId);
        return "redirect:/playLists";
    }
    @PostMapping("/playLists/new")
    public String savePlayList(@Valid @ModelAttribute("playList") PlayListDto playListDto,
                           BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("playList", playListDto);
            return "playList-create";
        }
        playListService.savePlayList(playListDto);
        return "redirect:/playLists";
    }
    @GetMapping("/playLists/{playListId}/edit")
    public String editPlayListForm(@PathVariable("playListId") Long playListId, Model model){
        PlayListDto playListDto = playListService.findPlayListById(playListId);
        model.addAttribute("playList", playListDto);
        return "playList-edit";
    }
    @PostMapping("/playLists/{playListId}/edit")
    public String updatePlayList(@PathVariable("playListId") Long clubId,
                             @Valid @ModelAttribute("club") PlayListDto playListDto,
                             BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("playList", playListDto);
            return "playList-edit";
        }
        playListDto.setId(clubId);
        playListService.updatePlayList(playListDto);
        return "redirect:/playLists";
    }



}
