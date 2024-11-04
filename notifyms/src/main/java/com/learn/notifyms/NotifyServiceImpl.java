package com.learn.notifyms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifyServiceImpl implements NotifyService{

    private final NotifyRepository notifyRepository;


}
