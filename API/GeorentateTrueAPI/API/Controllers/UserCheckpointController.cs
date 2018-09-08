using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using ClassLibrary.BusinessEntities;
using ClassLibrary.BusinessServices;

namespace API.Controllers
{
    public class UserCheckpointController : ApiController
    {
        IUserCheckpointServices userCheckpointServices = new UserCheckpointServices();

        /// <summary>
        /// UserCheckpoint.Add
        /// </summary>
        /// <param name="checkpoint"></param>
        /// <returns></returns>
        [HttpPost]
        public HttpResponseMessage Add(List<UserCheckpoint> checkpointList)
        {
            try
            {
                userCheckpointServices.Add(checkpointList);

                return Request.CreateResponse(HttpStatusCode.OK);
            }

            catch(Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }

        /// <summary>
        /// UserCheckpoint.Update
        /// </summary>
        /// <param name="checkpoint"></param>
        /// <returns></returns>
        [HttpPut]
        public HttpResponseMessage Update(UserCheckpoint checkpoint)
        {
            try
            {
                userCheckpointServices.Update(checkpoint);

                return Request.CreateResponse(HttpStatusCode.OK);
            }

            catch(Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }

        /// <summary>
        /// UserCheckpoint.Delete
        /// </summary>
        /// <param name="uid"></param>
        /// <param name="cid"></param>
        /// <returns></returns>
        [HttpGet]
        [Route("api/userCheckpoint/delete/{uid},{cid}")]
        public HttpResponseMessage Delete(int uid, int cid)
        {
            try
            {
                userCheckpointServices.Delete(uid, cid);

                return Request.CreateResponse(HttpStatusCode.OK);
            }

            catch(Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }

        /// <summary>
        /// UserCheckpoint.Get
        /// </summary>
        /// <param name="uid"></param>
        /// <param name="cid"></param>
        /// <returns></returns>
        [HttpGet]
        [Route("api/userCheckpoint/get/{uid},{cid}")]
        public HttpResponseMessage Get(int uid, int cid)
        {
            try
            {
                UserCheckpoint checkpoint = userCheckpointServices.Get(uid, cid);

                return Request.CreateResponse(HttpStatusCode.OK, checkpoint);
            }

            catch(Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }

        /// <summary>
        /// UserCheckpoint.getList
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [Route("api/userCheckpoint/getlist/{id}")]
        public HttpResponseMessage GetList(int id)
        {
            try
            {
                List<UserCheckpoint> list = userCheckpointServices.GetList(id);

                return Request.CreateResponse(HttpStatusCode.OK, list);
            }

            catch(Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }
    }
}
