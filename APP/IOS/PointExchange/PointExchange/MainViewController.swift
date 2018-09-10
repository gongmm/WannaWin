//
//  MainViewController.swift
//  PointExchange
//
//  Created by yiner on 2018/7/5.
//  Copyright © 2018年 WannaWin. All rights reserved.

import UIKit
import ESPullToRefresh

class MainViewController: UIViewController,ImageScrollerControllerDelegate {
    
    @IBOutlet weak var container: UIView!
	@IBOutlet weak var scrollView: UIScrollView!
	@IBOutlet weak var imageScrollerContainer: UIView!
	
    var activities:[OfflineActivity]?
    var coupons:[Item]?
    var isLoaded = false
	
	//图片轮播组件
	var imageScroller : ImageScrollerViewController!
    
    override func viewDidLoad() {
        ServerConnector.getAds(){(result,activities) in
            if result {
                self.activities = activities
                if self.isLoaded{
                    // TODO: 刷新轮播
					self.imageScroller.refresh()
					
                }else{
                    self.isLoaded = true
                }
            }
        }
        ServerConnector.getRecommendedItems(){(result, items) in
            if result {
                self.coupons = items
                if self.isLoaded{
                    // TODO: 刷新轮播
					self.imageScroller.refresh()
                }else{
                    self.isLoaded = true
                    
                }
            }
        }
    }

    override func viewWillAppear(_ animated: Bool) {
		super.viewWillAppear(animated)
		
		// 隐藏导航栏
		self.navigationController?.setNavigationBarHidden(true, animated: true)
		// 下拉加载
		scrollView.es.addPullToRefresh{ [unowned self] in
			
			self.scrollView.es.stopPullToRefresh(ignoreDate: true)
			self.scrollView.es.stopPullToRefresh(ignoreDate: true, ignoreFooter: false)
		}
        
    }
	
	// 设置滑动偏移量来隐藏刷新的白边
	override func viewWillLayoutSubviews() {
		scrollView.contentOffset = CGPoint(x: 0, y: 20)
	}
	
	// MARK: - 图片轮播组件协议
	//图片轮播组件协议方法：获取数据集合
	func scrollerDataSource() -> [String] {
		if self.activities != nil && self.coupons != nil {
			var items = [String]()
			
			items.append(self.activities![0].imageURL!)
			if (self.activities?.count)! > 1 {
				items.append(self.activities![1].imageURL!)
			}
			
			items.append(self.coupons![0].logoURL!)
			if (self.coupons?.count)! > 1 {
				items.append(self.coupons![1].logoURL!)
			}
			
			return items
		}
		else {
			return [String]()
		}
		
	}
	
	// MARK: - 所有点击事件的响应动作
	// TODO: - 点击图片响应事件
	@objc func handleTapAction(_ tap:UITapGestureRecognizer)->Void{
		//获取图片索引值
		let index = imageScroller.currentIndex
		//弹出索引信息
		let alertController = UIAlertController(title: "您点击的图片索引是：",
												message: "\(index)", preferredStyle: .alert)
		let cancelAction = UIAlertAction(title: "确定", style: .cancel, handler: nil)
		alertController.addAction(cancelAction)
		self.present(alertController, animated: true, completion: nil)
	}
	
    // MARK: - Navigation
    // 初始化图片轮播组件，为嵌入的图片轮播VC做数据准备
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
		if segue.identifier == "show scroller" {
			if segue.destination .isKind(of: ImageScrollerViewController.self){
				imageScroller = segue.destination as! ImageScrollerViewController
				imageScroller.delegate = self
				
				//为图片添加点击手势事件
				let tap = UITapGestureRecognizer(target: self, action: #selector(MainViewController.handleTapAction(_:)))
				imageScroller.view.addGestureRecognizer(tap)
			}
		}
        else if segue.identifier == "container" {
            

        }
    }

}
