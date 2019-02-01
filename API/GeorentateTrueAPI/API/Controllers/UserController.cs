using ClassLibrary.BusinessEntities;
using ClassLibrary.BusinessServices;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;

namespace API.Controllers
{
    public class UserController : ApiController
    {
        IUserServices userServices = new UserServices();
        
        /// <summary>
        /// User.add
        /// </summary>
        /// <param name="user"></param>
        /// <returns></returns>
        [HttpPost]
        [Route("api/user/add")]
        public HttpResponseMessage Add(User user)
        {
            try
            {
                int id = userServices.Add(user);

                return Request.CreateResponse(HttpStatusCode.OK, id);
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }

        /// <summary>
        /// User.update
        /// </summary>
        /// <param name="user"></param>
        /// <returns></returns>
        [HttpPut]
        public HttpResponseMessage Update(User user)
        {
            try
            {
                userServices.Update(user);

                return Request.CreateResponse(HttpStatusCode.OK);
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }

        /// <summary>
        /// User.delete
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpGet]
        [Route("api/user/delete/{id}")]
        public HttpResponseMessage Delete(int id)
        {
            try
            {
                userServices.Delete(id);

                return Request.CreateResponse(HttpStatusCode.OK);
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }

        /// <summary>
        /// User.get
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpGet]
        [Route("api/user/get/{id}")]
        public HttpResponseMessage Get(int id)
        {
            try
            {
                User user = userServices.Get(id);

                return Request.CreateResponse(HttpStatusCode.OK, user);
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }

        [HttpGet]
        public HttpResponseMessage GetList()
        {
            try
            {
                List<User> userList = userServices.GetList();

                return Request.CreateResponse(HttpStatusCode.OK, userList);
            }
            catch(Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }
      
        [HttpPost]
        [Route("api/user/logOn")]
        public HttpResponseMessage LogOn(User user)
        {
            try
            {
                User user1 = userServices.LoggingOnUser(user);

                return Request.CreateResponse(HttpStatusCode.OK, user1);
            }
            catch (Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }
    }
}