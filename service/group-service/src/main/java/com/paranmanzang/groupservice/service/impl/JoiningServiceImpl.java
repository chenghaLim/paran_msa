package com.paranmanzang.groupservice.service.impl;

import com.paranmanzang.groupservice.model.domain.ErrorField;
import com.paranmanzang.groupservice.model.domain.JoiningModel;
import com.paranmanzang.groupservice.model.entity.Group;
import com.paranmanzang.groupservice.model.entity.Joining;
import com.paranmanzang.groupservice.model.repository.GroupRepository;
import com.paranmanzang.groupservice.model.repository.JoiningRepository;
import com.paranmanzang.groupservice.service.JoiningService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JoiningServiceImpl implements JoiningService {
    private JoiningRepository joiningRepository;
    private GroupRepository groupRepository;

    public JoiningServiceImpl(JoiningRepository joiningRepository, GroupRepository groupRepository) {
        this.joiningRepository = joiningRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public Object addMember(JoiningModel joiningModel) {
        String nickname = joiningModel.getNickname();
        Long groupId = joiningModel.getGroupId();

        Optional<Joining> alreadyIn = joiningRepository.findJoiningByGroupIdAndNickname(groupId, nickname);
        if (alreadyIn.isPresent()) {
            return new ErrorField(nickname, "이미 가입되어있는 멤버입니다.");
        } else {
            Optional<Group> grouptoenter = groupRepository.findById(groupId);
            if (grouptoenter.isPresent()) {
                joiningModel.setGrouptojoin(grouptoenter.get());
                joiningModel.setNickname(nickname);
                return joiningRepository.save(joiningModel.toEntity()) != null ? Boolean.TRUE : Boolean.FALSE;
            } else {
                return new ErrorField(groupId, "group이 존재하지 않습니다.");
            }
        }
    }

    public Object enableMember(Long groupId, String nickname) {
        Optional<Joining> temp = joiningRepository.findByGroupIdAndNickname(groupId, nickname);
        if (temp.isPresent()) {
            Joining joiningToEnable = temp.get();
            if (!joiningToEnable.isEnabled()) {
                joiningToEnable.setEnabled(true);
               return joiningRepository.save(joiningToEnable) != null ? Boolean.TRUE : Boolean.FALSE;
            } else {
                return new ErrorField(nickname, "이미 승인된 멤버입니다.");
            }
        } else {
            return new ErrorField(nickname, "가입정보가 없습니다.");
        }

    }

    public Object disableMember(Long groupId, String nickname) {
        Optional<Joining> temp = joiningRepository.findByGroupIdAndNickname(groupId, nickname);
        if (temp.isPresent()) {
            Joining joiningToEnable = temp.get();
            if (joiningToEnable.isEnabled()) {
                joiningToEnable.setEnabled(false);
                return joiningRepository.save(joiningToEnable) != null ? Boolean.TRUE : Boolean.FALSE;
            } else {
                return new ErrorField(nickname, "미승인 멤버입니다.");
            }
        } else {
            return new ErrorField(nickname, "가입정보가 없습니다.");
        }

    }
}
