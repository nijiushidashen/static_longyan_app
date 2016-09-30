/**
 *my submit list view
 *我的提交列表
 **/
define('js/longyan/view/my_submit_list', [
        'text!js/longyan/template/my_submit_list.tpl',
        'text!js/longyan/template/my_submit_list_item.tpl',
        'js/util/memory_cache',
        'js/components/alert_ui',
        'js/element/view/header',
        'js/element/view/input-box',
        'js/element/view/button-box',
        'js/element/view/link-box',
        'js/element/view/tips-bar',
        'js/element/view/list-box',
        'js/api/report'
    ],
    function(ListContailerTpl, SubmitListItemTpl, Cache, AlertUI, HeaderView, InputBox, ButtonBox, LinkBox, TipsBar, ListBox, ReportApi) {
        var tipsAlert = tipsAlert || new AlertUI();
        var view_id = '#my-submit-list-view';
        var form_id = '#my-submit-list-form';
        var LayoutView = Backbone.View.extend({
            events: {
                'click .item-box': '_clickItem'
            },
            //
            initialize: function(options, config) {
                var t = this;
                t.config = config || {};
                t.$el.off('click');
                t.render();
                //加载数据       
                t.initEvents();
            },
            render: function() {
                var t = this;
                $('body').css('background-color', '#efeff4');
                t.$el.html(tpl(ListContailerTpl, {
                    config: t.config
                }));
                t.header_view = new HeaderView({
                    el: $('#header-container')
                }, {
                    text: '我的提交'
                });

                var i = 1;
                var mallId = t.config.id;
                t.list_box = new ListBox({
                    el: $('#my-submit-list-box')
                }, {
                    scroll: false //支持下拉刷新
                }, {
                    loadData: function(page, handler) {

                        var currentPage = 1;
                        var totalPages = 1;
                        var currentRecords = [{
                            id: 1,
                            name: '李晓明提交了万科十一区的小区变更申请',
                            dateStr: '2016-09-20',
                            timeStr: '21:22:14',
                            status: 0
                        }, {
                            id: 2,
                            name: '李晓明提交了<span>万科十一区</span>的小区变更申请',
                            dateStr: '2016-09-20',
                            timeStr: '21:22:14',
                            status: 0
                        }, {
                            id: 3,
                            name: '李晓明提交了万科十一区的小区变更申请',
                            dateStr: '2016-09-20',
                            timeStr: '21:22:14',
                            status: 0
                        }];
                        handler(currentRecords, currentPage, totalPages);
                    },
                    appendItem: function(data) {
                        console.log(data);
                        //住宅录入率
                        // var inputMemberRate = 0;
                        // if (data && data.inputCommunityRoomAmount) {
                        //     inputMemberRate = ((data.inputMemberAmount / data.inputCommunityRoomAmount) * 100).toFixed(0);
                        //     if (inputMemberRate > 100) {
                        //         inputMemberRate = 100;
                        //     }
                        // }



                        // var item = {
                        //     index: i,
                        //     name: data['xingMing'],
                        //     inputMemberAmount: data['inputMemberAmount'],
                        //     inputCommunityAmount: data['inputCommunityAmount'],
                        //     employeeCount: data['employeeCount'],
                        //     inputMemberRate: inputMemberRate,
                        //     url: '#report_employee_by_id/' + data['id']
                        // };
                        // i++;

                        return tpl(SubmitListItemTpl, {
                            data: data
                        });
                    }
                });
            },
            //初始化监听器
            initEvents: function() {
                var t = this;
            },

            _clickItem: function(e) {
                var t = this;
                var index = $(e.currentTarget).attr('index');
                if (index != t.config.status) {
                    window.location.href = '#my_submit_list/' + index;
                } else {
                    console.log("no action");
                }
            },

            destroy: function() {
                $(window).off('scroll');
            }
        });
        return LayoutView;
    });