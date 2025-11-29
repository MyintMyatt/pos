Hibernate: select p1_0.user_id,p1_0.permission from user_permissions p1_0 where p1_0.user_id=?
2025-11-17T13:45:26.875+06:30  WARN 21988 --- [pos] [nio-8080-exec-4] org.hibernate.orm.query                  : HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory
Hibernate:
select mi1_0.menu_id,mi1_0.fk_category_id,mi1_0.created_by,cb1_0.user_id,
cb1_0.created_date,cb1_0.deleted_date,cb1_0.is_account_is_active,cb1_0.is_account_not_locked,
cb1_0.is_deleted,cb1_0.is_enabled,cb1_0.password,cb1_0.profile_img_url,cb1_0.role,
cb1_0.update_date,cb1_0.user_email,cb1_0.user_name,mi1_0.created_date,mi1_0.description,
i1_0.inventory_id,i1_0.created_by,i1_0.created_date,i1_0.fk_menu_id,i1_0.quantity,i1_0.uom,
i1_0.updated_by,i1_0.updated_date,mi1_0.is_deleted,mi1_0.is_there_discount,mi1_0.menu_image_url,
mid1_0.fk_menu_id,mid1_0.id,mid1_0.created_by,mid1_0.created_date,d1_0.discount_id,d1_0.created_by,
d1_0.created_date,d1_0.deleted_date,d1_0.discount_type,d1_0.discount_value,d1_0.is_deleted,
d1_0.updated_by,d1_0.updated_date,d1_0.valid_from,d1_0.valid_to,mi1_0.menu_name,mi1_0.price,
ub3_0.user_id,ub3_0.created_date,ub3_0.deleted_date,ub3_0.is_account_is_active,ub3_0.is_account_not_locked,
ub3_0.is_deleted,ub3_0.is_enabled,ub3_0.password,ub3_0.profile_img_url,ub3_0.role,ub3_0.update_date,
ub3_0.user_email,ub3_0.user_name,mi1_0.updated_date
from tbl_menu_item mi1_0
join tbl_users cb1_0 on cb1_0.user_id=mi1_0.created_by
 left join tbl_inventory i1_0 on mi1_0.menu_id=i1_0.fk_menu_id
 left join tbl_menu_item_discount mid1_0 on mi1_0.menu_id=mid1_0.fk_menu_id
 left join tbl_discount d1_0 on d1_0.discount_id=mid1_0.fk_discount_id
 left join tbl_users ub3_0 on ub3_0.user_id=mi1_0.updated_by
 where mi1_0.is_deleted=false and (? is null or lower(mi1_0.menu_name) like lower(('%'||?||'%')) escape '') and (? is null or mi1_0.fk_category_id=?)



2025-11-17T13:45:26.970+06:30  WARN 21988 --- [pos] [nio-8080-exec-4] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 0, SQLState: 42883
2025-11-17T13:45:26.970+06:30 ERROR 21988 --- [pos] [nio-8080-exec-4] o.h.engine.jdbc.spi.SqlExceptionHelper   : ERROR: function lower(bytea) does not exist
  Hint: No function matches the given name and argument types. You might need to add explicit type casts.
  Position: 1488
2025-11-17T13:45:27.048+06:30  WARN 21988 --- [pos] [nio-8080-exec-4] .m.m.a.ExceptionHandlerExceptionResolver : Resolved [org.springframework.dao.InvalidDataAccessResourceUsageException: JDBC exception executing SQL [select mi1_0.menu_id,mi1_0.fk_category_id,mi1_0.created_by,cb1_0.user_id,cb1_0.created_date,cb1_0.deleted_date,cb1_0.is_account_is_active,cb1_0.is_account_not_locked,cb1_0.is_deleted,cb1_0.is_enabled,cb1_0.password,cb1_0.profile_img_url,cb1_0.role,cb1_0.update_date,cb1_0.user_email,cb1_0.user_name,mi1_0.created_date,mi1_0.description,i1_0.inventory_id,i1_0.created_by,i1_0.created_date,i1_0.fk_menu_id,i1_0.quantity,i1_0.uom,i1_0.updated_by,i1_0.updated_date,mi1_0.is_deleted,mi1_0.is_there_discount,mi1_0.menu_image_url,mid1_0.fk_menu_id,mid1_0.id,mid1_0.created_by,mid1_0.created_date,d1_0.discount_id,d1_0.created_by,d1_0.created_date,d1_0.deleted_date,d1_0.discount_type,d1_0.discount_value,d1_0.is_deleted,d1_0.updated_by,d1_0.updated_date,d1_0.valid_from,d1_0.valid_to,mi1_0.menu_name,mi1_0.price,ub3_0.user_id,ub3_0.created_date,ub3_0.deleted_date,ub3_0.is_account_is_active,ub3_0.is_account_not_locked,ub3_0.is_deleted,ub3_0.is_enabled,ub3_0.password,ub3_0.profile_img_url,ub3_0.role,ub3_0.update_date,ub3_0.user_email,ub3_0.user_name,mi1_0.updated_date from tbl_menu_item mi1_0 join tbl_users cb1_0 on cb1_0.user_id=mi1_0.created_by left join tbl_inventory i1_0 on mi1_0.menu_id=i1_0.fk_menu_id left join tbl_menu_item_discount mid1_0 on mi1_0.menu_id=mid1_0.fk_menu_id left join tbl_discount d1_0 on d1_0.discount_id=mid1_0.fk_discount_id left join tbl_users ub3_0 on ub3_0.user_id=mi1_0.updated_by where mi1_0.is_deleted=false and (? is null or lower(mi1_0.menu_name) like lower(('%'||?||'%')) escape '') and (? is null or mi1_0.fk_category_id=?)] [ERROR: function lower(bytea) does not exist<EOL>  Hint: No function matches the given name and argument types. You might need to add explicit type casts.<EOL>  Position: 1488] [n/a]; SQL [n/a]]


Hibernate:
select mi1_0.menu_id,mi1_0.fk_category_id,mi1_0.created_by,cb1_0.user_id,
cb1_0.created_date,cb1_0.deleted_date,cb1_0.is_account_is_active,cb1_0.is_account_not_locked,
cb1_0.is_deleted,cb1_0.is_enabled,cb1_0.password,cb1_0.profile_img_url,cb1_0.role,cb1_0.update_date,
cb1_0.user_email,cb1_0.user_name,mi1_0.created_date,mi1_0.description,i1_0.inventory_id,i1_0.created_by,
i1_0.created_date,i1_0.fk_menu_id,i1_0.quantity,i1_0.uom,i1_0.updated_by,i1_0.updated_date,mi1_0.is_deleted,
mi1_0.is_there_discount,mi1_0.menu_image_url,mid1_0.fk_menu_id,mid1_0.id,mid1_0.created_by,mid1_0.created_date,d1_0.discount_id,
d1_0.created_by,d1_0.created_date,d1_0.deleted_date,d1_0.discount_type,d1_0.discount_value,d1_0.is_deleted,d1_0.updated_by,d1_0.updated_date,d1_0.valid_from,
d1_0.valid_to,mi1_0.menu_name,mi1_0.price,ub3_0.user_id,ub3_0.created_date,ub3_0.deleted_date,ub3_0.is_account_is_active,ub3_0.is_account_not_locked,ub3_0.is_deleted,
ub3_0.is_enabled,ub3_0.password,ub3_0.profile_img_url,ub3_0.role,ub3_0.update_date,ub3_0.user_email,ub3_0.user_name,mi1_0.updated_date
from tbl_menu_item mi1_0
join tbl_users cb1_0 on cb1_0.user_id=mi1_0.created_by
left join tbl_inventory i1_0 on mi1_0.menu_id=i1_0.fk_menu_id left join tbl_menu_item_discount mid1_0 on mi1_0.menu_id=mid1_0.fk_menu_id left join tbl_discount d1_0 on d1_0.discount_id=mid1_0.fk_discount_id left join tbl_users ub3_0 on ub3_0.user_id=mi1_0.updated_by where mi1_0.is_deleted=false and (? is null or lower(cast(? as varchar)) is null or lower(mi1_0.menu_name) like lower(('%'||cast(? as varchar)||'%')) escape '') and (? is null or mi1_0.fk_category_id=?)

menu item before saving =>
MenuItem(menuId=null, menuName=Coca Cola, price=0.5,
category=null, menuImageUrl=null,
inventory=Inventory(inventoryId=null, menuItem=null, quantity=10, uom=Qty, createdDate=2025-11-17, createdBy=null, updatedDate=null, updatedBy=null),
menuItemDiscounts=null, isThereDiscount=false, description=energy drink, createdDate=2025-11-17,
 createdBy=User(userId=UID25110001,
 userEmail=user@example.com, userName=Orion,
  password=$2a$12$359igiu/HmEprp8ggRFZA.ZIaG52w8ywUKJcn6GHuolWJ8j2Eui0a,
   role=ADMIN, permissions=[DELETE, UPDATE, WRITE, READ], createdDate=2025-11-07,
    updateDate=null, isDeleted=false, deletedDate=null, isEnabled=true,
    isAccountIsActive=true, isAccountNotLocked=true,
     profileImgUrl=https://static.vecteezy.com/system/resources/previews/019/879/186/non_2x/user-icon-on-transparent-background-free-png.png),
      updatedDate=null, updatedBy=null, isDeleted=false)
